package ro.mpp2024.domain;

public class PersoanaOficiu extends Entity<Integer> {
    private String orasOficiu;
    private String username;
    private String password;

    public PersoanaOficiu(String orasOficiu, String username, String password) {
        this.orasOficiu = orasOficiu;
        this.username = username;
        this.password = password;
    }
    public String getOrasOficiu() {
        return orasOficiu;
    }
    public void setOrasOficiu(String orasOficiu) {
        this.orasOficiu = orasOficiu;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

}
