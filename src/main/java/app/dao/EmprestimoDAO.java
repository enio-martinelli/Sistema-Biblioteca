package app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import app.Domain.PacoteUsuarios.Funcionario;
import app.Domain.PacoteUsuarios.Leitor;
import app.Domain.PacoteObras.Copia;
import app.Domain.PacoteEntradaSaidaObras.Emprestimo;

public class EmprestimoDAO extends GenericDAO {

    private CopiaDAO cdao = new CopiaDAO();
    private LeitorDAO ldao = new LeitorDAO();
    private FuncionarioDAO fdao = new FuncionarioDAO();
    

    public void insert(Emprestimo emprestimo){
        String sql = "INSERT INTO emprestimo (dataEmprestimo, dataPrevistaDevolucao, funcionarioResponsavel, leitor, codigoCopia, atrasado) VALUES (?, ?, ?, ?, ?, ?) ";

        try{
            Connection conn = this.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);

            java.sql.Date mySQLDate = new java.sql.Date(emprestimo.getDataEmprestimo().getTime());
            statement.setDate(1, mySQLDate);
            java.sql.Date mySQLDate2 = new java.sql.Date(emprestimo.getDataPrevistaDevolucao().getTime());
            statement.setDate(2, mySQLDate2);
            
            //ATENCAO: Substituir pelo metodo getFuncionario().getId() quando este for implementado
            statement.setLong(3, emprestimo.getFuncionarioResponsavel().getId());
            statement.setLong(4, emprestimo.getLeitor().getId());
            statement.setLong(5, emprestimo.getCopia().getId());
            statement.setBoolean(6, emprestimo.getAtrasado());
            statement.executeUpdate();

            statement.close();
            conn.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void update(Emprestimo emprestimo) {
        String sql = "UPDATE emprestimo SET dataEmprestimo = ?, dataPrevistaDevolucao = ?, funcionarioResponsavel = ?, leitor = ?, codigoCopia = ?, atrasado = ? WHERE id = ?";

        try {
            Connection conn = this.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);

            java.sql.Date mySQLDate = new java.sql.Date(emprestimo.getDataEmprestimo().getTime());
            statement.setDate(1, mySQLDate);
            java.sql.Date mySQLDate2 = new java.sql.Date(emprestimo.getDataPrevistaDevolucao().getTime());
            statement.setDate(2, mySQLDate2);

            statement.setLong(3, emprestimo.getFuncionarioResponsavel().getId());
            statement.setLong(4, emprestimo.getLeitor().getId());
            statement.setLong(5, emprestimo.getCopia().getId());
            statement.setBoolean(6, emprestimo.getAtrasado());
            statement.setLong(7, emprestimo.getId());
            statement.executeUpdate();
            
            statement.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Emprestimo emprestimo){
        String sql = "DELETE FROM emprestimo where id = ?";

        try{
            Connection conn = this.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setLong(1, emprestimo.getId());
            statement.executeUpdate();

            statement.close();
            conn.close();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }


    public Emprestimo getById(Long id){
        Emprestimo emprestimo = null;

        String sql = "SELECT * from emprestimo WHERE id = ?";

        try{
            Connection conn = this.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                Date dataEmprestimo = resultSet.getDate("dataEmprestimo");
                Date dataPrevistaDevolucao = resultSet.getDate("dataPrevistaDevolucao");
                
                //ATENCAO: Adicionar quando funcionario for implementado
                //Funcionario funcionarioResponsavel = new Funcionario(resultSet.getLong("funcionarioResponsavel");
                //ou adicione 
                Funcionario funcionarioResponsavel = fdao.getById(resultSet.getLong("funcionarioResponsavel"));

                
                Leitor leitor = ldao.getById(resultSet.getLong("leitor"));
                Copia copia = cdao.getById(resultSet.getLong("codigoCopia"));
                boolean atrasado = resultSet.getBoolean("atrasado");

                emprestimo = new Emprestimo(id, dataEmprestimo, dataPrevistaDevolucao, funcionarioResponsavel, copia, leitor, atrasado);

            }
            resultSet.close();
            statement.close();
            conn.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return emprestimo;

    }

    public List<Emprestimo> getByLeitor(Long cpf){
        List<Emprestimo> listaEmprestimo = new ArrayList<>();
        
        
        String sql = "select * from emprestimo where id = (select idUsuario from leitor where documentoId = ?)";

        try{
            Connection conn = this.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            
            statement.setLong(1, cpf);
            ResultSet resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                Long id = resultSet.getLong("id");
                
                Date dataEmprestimo = resultSet.getDate("dataEmprestimo");
                Date dataPrevistaDevolucao = resultSet.getDate("dataPrevistaDevolucao");
                
                Funcionario funcionarioResponsavel = fdao.getById(resultSet.getLong("funcionarioResponsavel"));
                
                Leitor leitor = ldao.getById(resultSet.getLong("leitor"));
                Copia copia = cdao.getById(resultSet.getLong("codigoCopia"));
                boolean atrasado = resultSet.getBoolean("atrasado");

                Emprestimo emprestimo = new Emprestimo(id, dataEmprestimo, dataPrevistaDevolucao, funcionarioResponsavel, copia, leitor, atrasado);
                listaEmprestimo.add(emprestimo);

            }
            resultSet.close();
            statement.close();
            conn.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        return listaEmprestimo;
    }

    public Emprestimo getByLeitorAndCopia(Long idLeitor, Long idCopia){
        Emprestimo emprestimo = null;

        String sql = "SELECT * from emprestimo WHERE leitor = ? AND codigoCopia = ?";

        try{
            Connection conn = this.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setLong(1, idLeitor);
            statement.setLong(2, idCopia);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                Long id = resultSet.getLong("id");
                Date dataEmprestimo = resultSet.getDate("dataEmprestimo");
                Date dataPrevistaDevolucao = resultSet.getDate("dataPrevistaDevolucao");
                Funcionario funcionarioResponsavel = fdao.getById(resultSet.getLong("funcionarioResponsavel"));

                Leitor leitor = ldao.getById(idLeitor);
                Copia copia = cdao.getById(idCopia);
                boolean atrasado = resultSet.getBoolean("atrasado");

                emprestimo = new Emprestimo(id, dataEmprestimo, dataPrevistaDevolucao, funcionarioResponsavel, copia, leitor, atrasado);

            }
            resultSet.close();
            statement.close();
            conn.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return emprestimo;
    }


    public Emprestimo getByCopia(Long idCopia){
        Emprestimo emprestimo = null;

        String sql = "SELECT * from emprestimo WHERE codigoCopia = ?";

        try{
            Connection conn = this.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setLong(1, idCopia);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                Long id = resultSet.getLong("id");
                Long idLeitor = resultSet.getLong("leitor");
                Date dataEmprestimo = resultSet.getDate("dataEmprestimo");
                Date dataPrevistaDevolucao = resultSet.getDate("dataPrevistaDevolucao");
                Funcionario funcionarioResponsavel = fdao.getById(resultSet.getLong("funcionarioResponsavel"));

                Leitor leitor = ldao.getById(idLeitor);
                Copia copia = cdao.getById(idCopia);
                boolean atrasado = resultSet.getBoolean("atrasado");

                emprestimo = new Emprestimo(id, dataEmprestimo, dataPrevistaDevolucao, funcionarioResponsavel, copia, leitor, atrasado);

            }
            resultSet.close();
            statement.close();
            conn.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return emprestimo;
    }
}

