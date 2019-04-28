package br.edu.utfpr.dao;

import java.sql.Connection;
import java.sql.DriverManager;

import br.edu.utfpr.dto.PaisDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.java.Log;

@Log
public class PaisDAO extends AbstractDAO<PaisDTO>{

    // Responsável por criar a tabela País no banco
    public PaisDAO() {
        try ( Connection conn = DriverManager.getConnection("jdbc:derby:memory:database;create=true")) {

            log.info("Criando tabela pais ...");
            conn.createStatement().executeUpdate(
                    "CREATE TABLE pais ("
                    + "id int NOT NULL GENERATED ALWAYS AS IDENTITY CONSTRAINT id_pais_pk PRIMARY KEY,"
                    + "nome varchar(255),"
                    + "sigla varchar(3),"
                    + "codigoTelefone int)");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
      @Override
    public String preparaSQL(OperacaoEnum operacao) {
        switch(operacao){
            case inserir:
                return "INSERT INTO pais (nome, sigla, codigoTelefone) VALUES (?, ?, ?)";
            case alterar :
                return "UPDATE pais SET nome=?, sigla=?, codigoTelefone=? WHERE id=?";
            case deletar :
                return "DELETE FROM pais WHERE id=?";
            case listar :
                return "SELECT * FROM pais";
            default:
                return null;
        }
    }

    @Override
    public PreparedStatement preencheStatementInserir(PreparedStatement statement, PaisDTO pais) {
        try {
            statement.setString(1, pais.getNome());
            statement.setString(2, pais.getSigla());
            statement.setInt(3, pais.getCodigoTelefone());
        } catch (SQLException ex) {
            Logger.getLogger(PaisDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return statement;
    }
    
    @Override
    public PaisDTO populaLista(ResultSet result) {
        try {
            return PaisDTO.builder()
                    .id(result.getInt("id"))
                    .nome(result.getString("nome"))
                    .sigla(result.getString("sigla"))
                    .codigoTelefone(result.getInt("codigoTelefone"))
                    .build();
        } catch (SQLException ex) {
            Logger.getLogger(PaisDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return PaisDTO.builder().build();
    }

    @Override
    public PreparedStatement preencheStatementAlterar(PreparedStatement statement, PaisDTO pais) {
        try {
            statement.setString(1, pais.getNome());
            statement.setString(2, pais.getSigla());
            statement.setInt(3, pais.getCodigoTelefone());
            statement.setInt(4, pais.getId());
        } catch (SQLException ex) {
            Logger.getLogger(PaisDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return statement;
    }

}
