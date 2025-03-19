package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;

import controller.JdbcApp;
import dao.interfaces.ClientInterface;
import dto.ClientDto;

public class ClientDao implements ClientInterface {

    @Override
    public ClientDto getByName(String name) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getByName'");
    }

    @Override
    public ClientDto get(int id) throws SQLException {
        Connection con = JdbcApp.getConnection();
        PreparedStatement ps = con.prepareStatement("select * from clients where client_id = ?");
        ResultSet rs = ps.executeQuery();
        ClientDto client = null;
        if (rs.next()) {
            client = new ClientDto(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                    rs.getString(5), rs.getDate(6));
        }
        return client;
    }

    @Override
    public JSONArray getAll() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

    @Override
    public int save(ClientDto t) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public int insert(ClientDto t) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public int update(ClientDto t) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public int delete(ClientDto t) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

}
