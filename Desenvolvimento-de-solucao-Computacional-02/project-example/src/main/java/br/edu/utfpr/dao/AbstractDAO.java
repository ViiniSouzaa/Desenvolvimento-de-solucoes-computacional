/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.dao;

import br.edu.utfpr.dto.PaisDTO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vinicius
 * @param <T>
 */
public abstract class AbstractDAO<T> {
    
    public boolean inserir(T entidade) {
        try ( Connection conn = DriverManager.getConnection("jdbc:derby:memory:database")) {

            String sql = preparaSQL(OperacaoEnum.inserir);

            PreparedStatement statement = conn.prepareStatement(sql);
            int rowsInserted = preencheStatementInserir(statement, entidade).executeUpdate();

            if (rowsInserted > 0) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<PaisDTO> listarTodos() {

        List<PaisDTO> resultado = new ArrayList<>();

        try( Connection conn = DriverManager.getConnection("jdbc:derby:memory:database")) {

            String sql = preparaSQL(OperacaoEnum.listar);

            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql);

            while (result.next()) {
                resultado.add(populaLista(result));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }
    
        public boolean excluir(int id) {

        try ( Connection conn = DriverManager.getConnection("jdbc:derby:memory:database")) {

            String sql = preparaSQL(OperacaoEnum.deletar);

            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
        
    public boolean alterar(T entidade) {
        try ( Connection conn = DriverManager.getConnection("jdbc:derby:memory:database")){

            String sql = "UPDATE pais SET nome=?, sigla=?, codigoTelefone=? WHERE id=?";

            PreparedStatement statement = conn.prepareStatement(sql);
            statement = preencheStatementAlterar(statement, entidade);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) 
                return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public abstract String preparaSQL(OperacaoEnum operacao);

    public abstract PreparedStatement preencheStatementInserir(PreparedStatement statement,T entidade);

    public abstract PaisDTO populaLista(ResultSet result);

    public abstract PreparedStatement preencheStatementAlterar(PreparedStatement statement, T entidade);
}
