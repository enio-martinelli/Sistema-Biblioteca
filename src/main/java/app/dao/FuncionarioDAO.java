package app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import java.util.Date;

import app.Domain.PacoteUsuarios.Funcionario;
import app.Domain.PacoteUsuarios.Endereco;
import app.Domain.PacoteUsuarios.Usuario;

public class FuncionarioDAO extends GenericDAO {
    
    public void insert(Usuario funcionario){
        String sql = "INSERT INTO Usuario (nome, telefone, dataNascimento, endereco, role) VALUES (?, ?, ?, ?, funcionario) ";

        try{
            Connection conn = this.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);

            statement = conn.prepareStatement(sql);
            statement.setString(1, funcionario.getNome());
            statement.setString(2, funcionario.getTelefone());
            java.sql.Date mySQLDate = new java.sql.Date(funcionario.getDataNascimento().getTime());
            statement.setDate(3, mySQLDate);
            statement.setLong(4, funcionario.getEndereco().getId());
            statement.executeUpdate();

            statement.close();
            conn.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void update(Usuario funcionario) {
        String sql = "UPDATE Usuario SET nome = ?, telefone = ?, dataNascimento = ? WHERE id = ?";

        try {
            Connection conn = this.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setString(1, funcionario.getNome());
            statement.setString(2, funcionario.getTelefone());
            java.sql.Date mySQLDate = new java.sql.Date(funcionario.getDataNascimento().getTime());
            statement.setDate(3, mySQLDate);
            statement.setLong(4, funcionario.getId());
            statement.executeUpdate();

            statement.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Usuario funcionario){
        String sql = "DELETE FROM Usuario where id = ?";

        try{
            Connection conn = this.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setLong(1, funcionario.getId());
            statement.executeUpdate();

            statement.close();
            conn.close();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }


    public Usuario getById(Long id){
        Usuario funcionario = null;

        String sql = "SELECT * from Usuario WHERE id = ?";

        try{   
            Connection conn = this.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                String nome = resultSet.getString("nome");
                String telefone = resultSet.getString("telefone");
                Date dataNascimento = resultSet.getDate("dataNascimento");
                String role = "funcionario";

                Long enderecoId = resultSet.getLong("id");
                Endereco endereco = new EnderecoDAO().getById(enderecoId);


                funcionario = new Funcionario(id, nome, telefone, dataNascimento, endereco, role);
            }
            resultSet.close();
            statement.close();
            conn.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return funcionario;

    }
}
