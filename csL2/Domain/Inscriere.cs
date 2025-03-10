namespace lab2_mpp.domain;

public class Inscriere : Entity<int>
{
    public String ParticipantName { get; set; }
    public int ParticipantAge { get; set; }
    public Proba Proba { get; set; }

    public Inscriere(String participantName, int participantAge, Proba proba)
    {
        ParticipantName = participantName;
        ParticipantAge = participantAge;
        Proba = proba;
    }
    
    public override string ToString()
    =>$"Inscriere {{ name: {ParticipantName}; age: {ParticipantAge}; proba: {Proba} }}";
    
}