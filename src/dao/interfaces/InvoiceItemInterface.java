package dao.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;

public interface InvoiceItemInterface<InvoiceDto> {

    public ResultSet getInvoiceItems(int invoice_id) throws SQLException;

    public InvoiceDto addInvoiceItems(JSONArray d, int invoice_id) throws SQLException;

    public InvoiceDto deleteInvoiceItems(InvoiceDto d) throws SQLException;

    public InvoiceDto updateInvoiceItems(InvoiceDto d) throws SQLException;

}
