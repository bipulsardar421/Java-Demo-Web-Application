package dao.interfaces;

import java.sql.SQLException;
import java.util.List;

public interface GenericDao<T> {

    T getByName(String name) throws SQLException;

    T get( int id) throws SQLException;

    List<T> getAll() throws SQLException;

    int save(T t) throws SQLException;

    int insert(T t) throws SQLException;

    int update(T t) throws SQLException;

    int delete(T t);

}
