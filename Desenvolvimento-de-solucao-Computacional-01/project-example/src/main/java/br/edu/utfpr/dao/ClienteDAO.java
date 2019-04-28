package br.edu.utfpr.dao;

import java.sql.Connection;
import java.sql.DriverManager;

import br.edu.utfpr.dto.ClienteDTO;
import br.edu.utfpr.dto.PaisDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.java.Log;

@Log
public class ClienteDAO {

    // Responsável por criar a tabela Cliente no banco.
    public ClienteDAO() {

        try (Connection conn = DriverManager.getConnection("jdbc:derby:memory:database;create=true")) {

            log.info("Criando tabela cliente ...");
            conn.createStatement().executeUpdate(
            "CREATE TABLE cliente (" +
			"id int NOT NULL GENERATED ALWAYS AS IDENTITY CONSTRAINT id_cliente_pk PRIMARY KEY," +
			"nome varchar(255)," +
			"telefone varchar(30)," + 
			"idade int," + 
                        "limiteCredito double," +
                        "id_pais int)");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void insert(ClienteDTO cliente){
        try (Connection conn = DriverManager.getConnection("jdbc:derby:memory:database;create=true")) {
            String sql = "INSERT INTO cliente (id,nome,telefone,idade,limiteCredito,id_pais) VALUES (?,?,?,?,?,?)";
            
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, cliente.getId());
            statement.setString(2, cliente.getNome());
            statement.setString(3, cliente.getTelefone());
            statement.setInt(4, cliente.getIdade());
            statement.setDouble(5, cliente.getLimiteCredito());
            statement.setInt(6, cliente.getPais().getId());
            
            int contRows = statement.executeUpdate();
            if(contRows > 0)
                System.out.println("Um novo cliente foi inserido com sucesso!");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void update(ClienteDTO cliente){
        try (Connection conn = DriverManager.getConnection("jdbc:derby:memory:database;create=true")){
            String sql =  "UPDATE cliente SET nome=?, telefone=?,idade=?,limiteCredito=?,id_pais=? WHERE id=?";
            
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, cliente.getNome());
            statement.setString(2, cliente.getTelefone());
            statement.setInt(3, cliente.getIdade());
            statement.setDouble(4, cliente.getLimiteCredito());
            statement.setInt(5, cliente.getPais().getId());
            statement.setInt(6, cliente.getId());
           
            int contRows = statement.executeUpdate();
            if(contRows > 0)
                System.out.println("O cliente foi alterado com sucesso!");
            
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void delete(int id){
        try (Connection conn = DriverManager.getConnection("jdbc:derby:memory:database;create=true")){
            String sql =  "DELETE FROM cliente WHERE id=?";
            
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            
            int contRows = statement.executeUpdate();
            if (contRows > 0) 
                System.out.println("O cliente foi deletado com sucesso!");
            
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public List<ClienteDTO> getAll(){
        try (Connection conn = DriverManager.getConnection("jdbc:derby:memory:database;create=true")){
            List<ClienteDTO> list =  new ArrayList<>();
            String sql = "SELECT * FROM cliente join pais on pais.id = id_pais";
            
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
       
            while(rs.next()){
                ClienteDTO cliente = ClienteDTO.builder().build();
                cliente.setId(rs.getInt("id"));
                cliente.setNome(rs.getString("cliente.nome"));
                cliente.setTelefone(rs.getString("telefone"));
                cliente.setIdade(rs.getInt("idade"));
                cliente.setLimiteCredito(rs.getDouble("limiteCredito"));
                cliente.setPais(PaisDTO.builder().id(rs.getInt("pais.id"))
                                .nome("pais.nome").sigla("sigla")
                                .codigoTelefone(rs.getShort("codigoTelefone"))
                                .build());
                list.add(cliente);
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public ClienteDTO getById(int id){
        try (Connection conn = DriverManager.getConnection("jdbc:derby:memory:database;create=true")){
            ClienteDTO cliente = ClienteDTO.builder().build();
            String sql = "SELECT * FROM cliente join pais on pais.id = id_pais WHERE id=?";
            
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            
            ResultSet rs = statement.executeQuery(sql);
       
            while(rs.next()){
                cliente.setId(rs.getInt("id"));
                cliente.setNome(rs.getString("cliente.nome"));
                cliente.setTelefone(rs.getString("telefone"));
                cliente.setIdade(rs.getInt("idade"));
                cliente.setLimiteCredito(rs.getDouble("limiteCredito"));
                cliente.setPais(PaisDTO.builder().id(rs.getInt("pais.id"))
                                .nome("pais.nome").sigla("sigla")
                                .codigoTelefone(rs.getShort("codigoTelefone"))
                                .build());
               
            }
            return cliente;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
    