package dao.GenericDao;

import java.sql.SQLException;

import org.json.JSONArray;



public interface GenericDao<T> {

    T getByName(String name) throws SQLException;

    T get( int id) throws SQLException;

    JSONArray getAll() throws SQLException;

    int save(T t) throws SQLException;

    int insert(T t) throws SQLException;

    int update(T t) throws SQLException;

    int delete(T t) throws SQLException;

}
