package dao.dto;

public class LoginDto {

    private int id;
    private String username;
    private String password;

    public LoginDto(int id, String password, String username) {
        this.id = id;
        this.password = password;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LoginDto{");
        sb.append("id=").append(id);
        sb.append(", username=").append(username);
        sb.append(", password=").append(password);
        sb.append('}');
        return sb.toString();
    }

}
