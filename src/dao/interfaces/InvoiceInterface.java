package dao.interfaces;

import java.sql.SQLException;

import org.json.JSONArray;

import dao.GenericDao.GenericDao;
import dto.invoice.InvoiceDto;

public interface InvoiceInterface extends GenericDao<InvoiceDto> {
    public JSONArray getWithUserId(int userId) throws SQLException;
}
