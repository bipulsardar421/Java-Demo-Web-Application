package dao.interfaces;

import java.sql.Date;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

public interface ReportInterface {
    public JSONArray generateReport(Date startDate, Date endDate) throws SQLException;
    public JSONObject getCombinedReport(Date startDate, Date endDate) throws SQLException;
}
