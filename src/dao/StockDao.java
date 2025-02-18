package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.json.JSONArray;
import java.sql.ResultSet;

import controller.JdbcApp;
import dao.interfaces.StockInterface;
import dto.stock.stockDto;
import handler.resultset_handler.JsonResultset;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class StockDao implements StockInterface {

    @Override
    public stockDto getByName(String name) throws SQLException {
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
        Connection con = JdbcApp.getConnection();
        String qry = "select * from stock where status = 'active'";
        PreparedStatement ps = con.prepareStatement(qry);
        ResultSet rs = ps.executeQuery();
        return JsonResultset.convertToJson(rs);
    }

    @Override
    public int insert(stockDto t) throws SQLException {
        Connection con = JdbcApp.getConnection();
        String qry = "insert into stock (product_name, quantity, rate, recieved_date, image) value (?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(qry);
        ps.setString(1, t.getPname());
        ps.setInt(2, t.getQty());
        ps.setInt(3, t.getRate());
        ps.setDate(4, t.getR_date());
        ps.setString(5, t.getImg());
        int result = ps.executeUpdate();
        return result;
    }

    @Override
    public int update(stockDto t) throws SQLException {
        Connection con = JdbcApp.getConnection();
        PreparedStatement ps;
        String updateQry = "update stock set product_name = ?, quantity= ?, rate = ?, recieved_date = ?, image = ?, updatedAt = ? where product_id = ?";
        ps = con.prepareStatement(updateQry);
        ps.setString(1, t.getPname());
        ps.setInt(2, t.getQty());
        ps.setInt(3, t.getRate());
        ps.setDate(4, t.getR_date());
        ps.setString(5, t.getImg());
        ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
        ps.setInt(7, t.getPid());
        int result = ps.executeUpdate();
        return result;
    }

    public String getImageName(int id) throws SQLException {
        String img = "";
        String query = "SELECT image FROM stock WHERE product_id = ?";
        try (Connection con = JdbcApp.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    img = rs.getString("image");
                }
            }
        }
        return img;
    }

    @Override
    public int delete(stockDto t) throws SQLException {
        Connection con = JdbcApp.getConnection();
        String qry = "UPDATE stock SET status = 'inactive' WHERE product_id = ?";
        PreparedStatement ps = con.prepareStatement(qry);
        ps.setInt(1, t.getPid());
        int result = ps.executeUpdate();
        return result;
    }

    @Override
    public int save(stockDto t) throws SQLException {
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

}
