namespace lab2_mpp.domain;

public class PersoanaOficiu : Entity<int>
{
    public String OrasOficiu { get; set; }
    public String Username { get; set; }
    public String Password { get; set; }

    public PersoanaOficiu(String orasOficiu, String username, String password)
    {
        OrasOficiu = orasOficiu;
        Username = username;
        Password = password;
    }
    
    public override String ToString()
    => $"PersoanaOficiu {{ orasOficiu= {OrasOficiu}, user= {Username}, pass={Password} }}";
}