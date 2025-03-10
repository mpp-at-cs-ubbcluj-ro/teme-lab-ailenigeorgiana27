namespace lab2_mpp.domain;

public class Participant
{
    public String Name { get; set; }
    public int Age { get; set; }
    public int NrProbe { get; set; }

    public Participant(String name, int age, int nrProbe)
    {
        Name = name;
        Age = age;
        NrProbe = nrProbe;
    }
    
    public override string ToString()
    =>$"Participant {{ name: {Name}, age: {Age}, nrProbe: {NrProbe}}}";
}