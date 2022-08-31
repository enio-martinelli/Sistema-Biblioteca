package app.Domain.PacoteObras;

import app.Domain.PacoteObras.Estados.*;

public class Copia {
	private Long id;
	private State state;
    private Long obraId;
	
	public Copia(State state, Long obraId) {
		this.state = state;
        this.obraId = obraId;
	}

    public Copia(Long id, State state, Long obraId) {
        this(state, obraId);
        this.id = id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return this.state;
    }

    public void setObraId(Long obraID){
        this.obraId = obraID;
    }

    public Long getObraId() {
        return this.obraId;
    }
	
	public void emprestar() {
        
	}
	
	public void devolver() {
		
	}
	
	public void reservar() {
		
	}
	
	public State estadoAtual() {
		return this.state;
	}

    
}