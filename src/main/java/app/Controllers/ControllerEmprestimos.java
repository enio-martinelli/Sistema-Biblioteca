package app.Controllers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import app.Domain.PacoteEntradaSaidaObras.Emprestimo;
import app.Domain.PacoteEntradaSaidaObras.Reserva;
import app.Domain.PacoteObras.Copia;
import app.Domain.PacoteObras.Obra;
import app.Domain.PacoteUsuarios.Funcionario;
import app.Domain.PacoteUsuarios.Leitor;
import app.Domain.SubjectObserver.EmprestimoAtrasado;
import app.Domain.SubjectObserver.Subject;
import app.Exception.AnnotatedDeserializer;
import app.Service.impl.CopiaService;
import app.Service.impl.EmprestimoService;
import app.Service.impl.ReservaService;
import app.Service.impl.FuncionarioService;
import app.Service.impl.LeitorService;
import app.Service.impl.ObraService;
import app.Service.spec.ICopiaService;
import app.Service.spec.IEmprestimoService;
import app.Service.spec.IFuncionarioService;
import app.Service.spec.IReservaService;
import app.Service.spec.ILeitorService;
import app.Service.spec.IObraService;
import app.Integracao.Integracao;
import app.StandardResponse.StandardResponse;
import app.StandardResponse.StatusResponse;
import spark.Request;
import spark.Response;
import spark.Route;

public class ControllerEmprestimos {

    private static IEmprestimoService emservice = new EmprestimoService();
    private static IReservaService rservice = new ReservaService();
    private static IFuncionarioService fservice = new FuncionarioService();
    private static ILeitorService lservice = new LeitorService();
    private static ICopiaService cservice = new CopiaService();
    private static IObraService oservice = new ObraService();
    private static Subject listaEmprestimos = new Subject();

    private static Gson gsonEmprestimo() {
        return new GsonBuilder()
        .registerTypeAdapter(Emprestimo.class, new AnnotatedDeserializer<Emprestimo>())
        .create();
    }
    
    public static Route buscaEmprestimosPorUsuario = (Request req, Response res) -> {
        Long documento = Long.parseLong(req.params(":cpf"));
        Leitor leitor = lservice.buscaPorDocumento(documento);
        if (leitor == null){
            return new StandardResponse(StatusResponse.ERROR, "Usuario não encontrado");
        }
        List<Emprestimo> lista = emservice.buscaEmprestimos(leitor.getId());
        
        return new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(lista));
        
    };

    /*
     * Busca por emprestimos atrasados e retorna a quantidade de pendencias
     */
    public static Route buscaPendenciasPorUsuario = (Request req, Response res) -> {
        Long ra = Long.parseLong(req.params(":ra"));
        Leitor leitor = lservice.buscaPorDocumento(ra);
        if(leitor == null){
            //retorna 0 caso o usuario não esteja no banco de dados da biblioteca
            int i = 0;
            return new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(i));
        }
        List<Emprestimo> lista = emservice.buscaEmprestimos(leitor.getId());
        int acm = 0;
        for (Emprestimo emprestimo : lista){
            if (emprestimo.getAtrasado()){
                //emprestimo atrasado
                System.out.println("aqui");
                acm += 1;
            }
        }
        return new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(acm));
    };

    /*
     * Realiza o Emprestimo de uma Obra
     */
    public static Route emprestarObra = (Request req, Response res) -> {
        res.type("application/json");
        Gson gson = gsonEmprestimo();
        Emprestimo emprestimo = gson.fromJson(req.body(), Emprestimo.class);
        Long documento = Long.parseLong(req.params(":cpf"));
        
        Copia copia = cservice.buscaCopia(Long.parseLong(req.params(":idCopia")));
        Leitor leitor = emprestimo.getLeitor();
        Funcionario funcionario = new Funcionario(emprestimo.getFuncionarioResponsavel().getId());
        Obra obra = oservice.buscaObraPorCodigo(emprestimo.getCopia().getObraId());
        leitor = lservice.buscaPorDocumento(documento);
        funcionario = fservice.getFuncionario(funcionario.getId());
        
        //verifica se as informações são válidas
        if (copia == null || leitor == null || funcionario == null){
            return new StandardResponse(StatusResponse.ERROR, "[ERRO] Dados inválidos! Retornando NULL.");

        }

        //verifica se o leitor está inscrito em alguma disciplina e se está ou esteve em algum grupo acadêmico
        int numDisciplina = Integracao.getDisciplina(leitor.getDocumentoId());
        int grupoAtual = Integracao.getGrupo(leitor.getDocumentoId(), 1);
        int grupoDesativado = Integracao.getGrupo(leitor.getDocumentoId(), 0);
        if(numDisciplina == -1 || grupoAtual == -1 || grupoDesativado == -1){
            return new StandardResponse(StatusResponse.ERROR, "[ERRO] Não foi possível consultar disciplinas/grupos para este RA");
        }
        if(numDisciplina == 0){
            return new StandardResponse(StatusResponse.ERROR, "[EROO] Estudante não está inscrito em disciplinas, não é possível emprestar");
        }
        if(grupoAtual == 0 && grupoDesativado == 0){
            return new StandardResponse(StatusResponse.ERROR, "[EROO] Estudante nunca participou de um grupo acadêmico, não é possível emprestar");
        }
    
        
        LocalDate dataDevolucao = obra.getCategoria().calculaDataDevolucao();
        java.util.Date dataPrevistaDevolucao = java.util.Date.from(dataDevolucao.atStartOfDay(ZoneId.systemDefault()).toInstant());
        java.util.Date dataEmprestimo = java.util.Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Long idUsuario = leitor.getId();

        //obs o id do emprestimo nao é usado, o bd gera um
        emprestimo.setDataEmprestimo(dataEmprestimo);
        emprestimo.setDataPrevistaDevolucao(dataPrevistaDevolucao);
        emprestimo.setAtrasado(false);
        
        //verifica se há uma reserva antes de realizar o emprestimo
        Reserva reserva = rservice.buscarPorLeitorECopia(idUsuario, copia.getId());
        if(reserva != null){
            //caso exista uma reserva e não seja deste leitor
            if(reserva.getLeitor().getId() != idUsuario) {
                return new StandardResponse(StatusResponse.ERROR, "[ERRO] Cópia já está reservada por outro usuário, não é possível emprestar!");
            }

            //caso a reserva seja deste leitor
            else{
                //checa o estado da cópia, se for emprestado não pode realizar a reserva
                if(copia.getState().getState() == "Emprestado"){
                    return new StandardResponse(StatusResponse.ERROR, "[ERRO] Copia ainda está emprestada, espere o término do empŕestimo");
                }
               
                //criando um emprestimo
                new EmprestimoAtrasado(emprestimo, reserva.getLeitor());
                emservice.realizarEmprestimo(emprestimo);
                copia.emprestar();
                cservice.alterarCopia(copia.getId(), copia);
                new EmprestimoAtrasado(listaEmprestimos, leitor);
                return new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(emprestimo));
            }
        }
        

        if(copia.getState().getState() != "Disponivel") {
            return new StandardResponse(StatusResponse.ERROR, "[ERRO] Copia não está disponível, impossível emprestar!");
        }
        
        
        emservice.realizarEmprestimo(emprestimo);
        copia.emprestar();
        cservice.alterarCopia(copia.getId(), copia);
        new EmprestimoAtrasado(listaEmprestimos, leitor);
        listaEmprestimos.notifyAllObservers();
        return new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(emprestimo));
    };
    
}
