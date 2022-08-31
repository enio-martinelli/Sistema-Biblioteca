package app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import app.Leitor.Leitor;

public class LeitorDAO extends GenericDAO {
    
    public void insert(Leitor leitor){
        String sql = "INSERT INTO Leitor (email, documentoId, grupoAcademico, categoria) VALUES (?, ?, ?, ?) ";

        try{
            Connection conn = this.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);

            statement = conn.prepareStatement(sql);
            statement.setString(1, leitor.getEmail());
            statement.setString(2, leitor.getDocumentoId());
            statement.setBoolean(3, leitor.getGrupoAcademico());
            statement.setLong(4, leitor.getCategoria().getId());
            statement.executeUpdate();

            statement.close();
            conn.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void update(Leitor leitor) {
        String sql = "UPDATE Leitor SET email = ?, documentoId = ?, grupoAcademico = ? WHERE id = ?";

        try {
            Connection conn = this.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setString(1, leitor.getEmail());
            statement.setString(2, leitor.getDocumentoId());
            statement.setBoolean(3, leitor.getGrupoAcademico());
            statement.setLong(4, leitor.getId());
            statement.executeUpdate();

            statement.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Leitor leitor){
        String sql = "DELETE FROM Leitor where id = ?";

        try{
            Connection conn = this.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setLong(1, leitor.getId());
            statement.executeUpdate();

            statement.close();
            conn.close();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }


    public Leitor getById(Long id){
        Leitor Leitor = null;

        String sql = "SELECT * from Leitor WHERE id = ?";

        try{   
            Connection conn = this.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                String email = resultSet.getString("email");
                String documentoId = resultSet.getString("documentoId");
                boolean grupoAcademico = resultSet.getBoolean("grupoAcademico");

                Leitor = new Leitor(id, email, documentoId, grupoAcademico);
            }
            resultSet.close();
            statement.close();
            conn.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return Leitor;

    }
}