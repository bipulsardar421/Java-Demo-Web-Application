package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.json.JSONArray;
import java.sql.ResultSet;

import controller.JdbcApp;
import dao.interfaces.login.StockInterface;
import dto.stock.stockDto;

public class StockDao implements StockInterface {

    @Override
    public stockDto getByName(String name) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getByName'");
    }

    @Override
    public stockDto get(int id) throws SQLException {
        Connection con = JdbcApp.getConnection();
        String qry = "select * from stock where product_id = ?";
        PreparedStatement ps = con.prepareStatement(qry);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        stockDto st = new stockDto(rs);
        return st;
    }

    @Override
    public JSONArray getAll() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

    @Override
    public int save(stockDto t) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public int insert(stockDto t) throws SQLException {
      Connection con = JdbcApp.getConnection();
        String qry = "insert into stock (product_name, quantity, rate, image) value (?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(qry);
        ps.setString(1, t.getPname());
        ps.setInt(2, t.getQty());
        ps.setInt(3, t.getRate());
        ps.setString(4, t.getImg());
        int result = ps.executeUpdate();
        return result;
    }

    @Override
    public int update(stockDto t) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public int delete(stockDto t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
    
}
