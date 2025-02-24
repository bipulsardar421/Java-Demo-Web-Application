package dto.stock;

import java.sql.Date;
import java.sql.ResultSet;

public class stockDto {

    int pid;
    String pname;
    int qty;
    int rate;
    Date r_date;
    String img;
    String status;
    ResultSet data;

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public Date getR_date() {
        return r_date;
    }

    public void setR_date(Date r_date) {
        this.r_date = r_date;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public stockDto(int pid, String pname, int qty, int rate, Date r_date, String img, String status) {
        this.pid = pid;
        this.pname = pname;
        this.qty = qty;
        this.rate = rate;
        this.r_date = r_date;
        this.img = img;
        this.status = status;
    }

    public stockDto(ResultSet rs) {
        this.data = rs;
    }

    public ResultSet getData() {
        return data;
    }
    
}
