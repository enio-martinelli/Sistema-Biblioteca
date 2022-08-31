package app.Obra;
import static spark.Spark.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import app.StandardResponse.StandardResponse;
import app.StandardResponse.StatusResponse;
import spark.Request;
import spark.Response;
import spark.Route;

public class ControllerObras {

    private static IObraService service = new ObraService();

    /*
     * Busca todas as Obras
     */
    public static Route buscaObras = (Request req, Response res) -> {
        // TODO: Implementar
        res.status(501);
        return new StandardResponse(StatusResponse.ERROR, "Não Implementado");
    };

    /*
     * Busca uma obra por ISBN
     */
    public static Route buscaObra = (Request req, Response res) -> {
        try {
            BigInteger isbn = new BigInteger(req.params(":isbn"));
            Obra obra = service.buscaObra(isbn);
            return new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(obra));
        } catch (NumberFormatException e) {
            System.out.println("Erro ao converter \"" + req.params(":isbn") + "\" em bigint");
            res.status(400);
            return new StandardResponse(StatusResponse.ERROR, "ISBN inválido");
        }
    };

    /*
     * Adiciona uma obra
     */
    public static Route adicionarObra = (Request req, Response res) -> {
        // TODO: Implementar
        res.status(501);
        return new StandardResponse(StatusResponse.ERROR, "Não Implementado");
    };

    /*
     * Remove uma obra por ISBN
     */
    public static Route removerObra = (Request req, Response res) -> {
        // TODO: Implementar
        res.status(501);
        return new StandardResponse(StatusResponse.ERROR, "Não Implementado");
    };

    /*
     * Adiciona um autor
     */
    public static Route adicionarAutor = (Request req, Response res) -> {
        // TODO: Implementar
        res.status(501);
        return new StandardResponse(StatusResponse.ERROR, "Não Implementado");
    };
    
    /*
     * Altera um autor
     */
    public static Route alterarAutor = (Request req, Response res) -> {
        // TODO: Implementar
        res.status(501);
        return new StandardResponse(StatusResponse.ERROR, "Não Implementado");
    };

    /*
     * Remove um autor por nome
     */
    public static Route removerAutor = (Request req, Response res) -> {
        // TODO: Implementar
        res.status(501);
        return new StandardResponse(StatusResponse.ERROR, "Não Implementado");
    };

    /*
     * Altera disponibilidade de uma cópia por código
     */
    public static Route mudarDisponibilidadeCopia = (Request req, Response res) -> {
        // TODO: Implementar
        res.status(501);
        return new StandardResponse(StatusResponse.ERROR, "Não Implementado");
    };
}
