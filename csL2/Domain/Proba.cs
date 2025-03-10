namespace lab2_mpp.domain;

public class Proba :Entity<int>
{
    public string Distanta { get; set; }
    public string Stil { get; set; }
    public int NrParticipants { get; set; }

    public Proba(string distanta, string stil, int nrParticipants)
    {
        Distanta = distanta;
        Stil = stil;
        NrParticipants = nrParticipants;
    }
    
    public override string ToString()
    => $"Proba: {{dist = {Distanta}, stil = {Stil}, part = {NrParticipants} }}";
}