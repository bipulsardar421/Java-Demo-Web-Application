package dao.interfaces;

import dto.login.LoginDto;
import java.sql.SQLException;

import org.json.JSONArray;


public interface GenericDao<T> {

    LoginDto getByName(String name) throws SQLException;

    JSONArray get( int id) throws SQLException;

    JSONArray getAll() throws SQLException;

    int save(T t) throws SQLException;

    int insert(T t) throws SQLException;

    int update(T t) throws SQLException;

    int delete(T t);

}
