package dbmodel.login;

import dao.dto.LoginDto;
import dao.impl.login.LoginImpl;
import dao.interfaces.login.LoginInterface;
import java.sql.SQLException;

public class LoginDBmodel {
public static void main(String[] args) throws SQLException {
  LoginInterface li = new LoginImpl();
  LoginDto ld = li.getByName("b@c.c");
  System.out.println(ld);
}
}
