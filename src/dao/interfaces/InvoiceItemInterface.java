package dao.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface InvoiceItemInterface<InvoiceDto> {

    public ResultSet getInvoiceItems(int invoice_id) throws SQLException;

    public InvoiceDto addInvoiceItems(InvoiceDto d) throws SQLException;

    public InvoiceDto deleteInvoiceItems(InvoiceDto d) throws SQLException;

    public InvoiceDto updateInvoiceItems(InvoiceDto d) throws SQLException;

}
