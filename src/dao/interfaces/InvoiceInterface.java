package dao.interfaces;

import java.sql.SQLException;

import org.json.JSONArray;

import dao.GenericDao.GenericDao;
import dto.invoice.InvoiceDto;

public interface InvoiceInterface extends GenericDao<InvoiceDto> {
    public JSONArray getWithUserId(long phone) throws SQLException;

    JSONArray search(long phone, long searchParam) throws SQLException;
}
