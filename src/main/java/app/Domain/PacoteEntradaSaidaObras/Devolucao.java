package app.Domain.PacoteEntradaSaidaObras;

import java.util.Date;
import app.Exception.AnnotatedDeserializer.JsonRequired;

public class Devolucao {
    private Long id;

    @JsonRequired
    private Date dataDevolucao;

    @JsonRequired
    private double multaTotal;

    @JsonRequired
    private Emprestimo emprestimoCorrespondente;

    public Devolucao(Long id, Date dataDevolucao, double multaTotal, Emprestimo emprestimoCorrespondente) {
        this.id = id;
        this.dataDevolucao = dataDevolucao;
        this.multaTotal = multaTotal;
        this.emprestimoCorrespondente = emprestimoCorrespondente;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setDataDevolucao(Date dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public Date getDataDevolucao() {
        return this.dataDevolucao;
    }

    public void setMultaTotal(double multaTotal) {
        this.multaTotal = multaTotal;
    }

    public double getMultaTotal() {
        return this.multaTotal;
    }

    public void setEmprestimoCorrespondente(Emprestimo emprestimo) {
        this.emprestimoCorrespondente = emprestimo;
    }

    public Emprestimo getEmprestimoCorrespondente() {
        return this.emprestimoCorrespondente;
    }
}
