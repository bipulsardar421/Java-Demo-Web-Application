package dao.interfaces;

import java.sql.Date;
import java.sql.SQLException;

import org.json.JSONArray;

public interface ReportInterface {
    public JSONArray getReports(Date startDate, Date endDate, String role, int userId) throws SQLException;
}
