package dao.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;

import dao.GenericDao.GenericDao;
import dto.login.LoginDto;

public interface LoginInterface extends GenericDao<LoginDto> {

    public ResultSet getUsersData(int id) throws SQLException;
    public int[] editUserRole(JSONArray jdata) throws SQLException;

}
