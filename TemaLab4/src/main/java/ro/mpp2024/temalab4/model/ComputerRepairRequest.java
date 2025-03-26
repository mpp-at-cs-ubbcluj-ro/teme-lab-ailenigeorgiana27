package ro.mpp2024.temalab4.model;

import java.io.Serializable;

public  class ComputerRepairRequest implements ro.mpp2024.temalab4.model.Identifiable<Integer>, Serializable {


    private int ID;
    private String ownerName;
    private  String ownerAddress;
    private String phoneNumber;
    private String model;
    private  String date;
    private  String problemDescription;
    private ro.mpp2024.temalab4.model.RequestStatus status;

    public ComputerRepairRequest(){

        this.ID = 0;
        this.ownerName = "";
        this.ownerAddress = "";
        this.phoneNumber = "";
        this.model = "";
        this.date ="";
        this.problemDescription = "";
        status= ro.mpp2024.temalab4.model.RequestStatus.New;
    }
    public ComputerRepairRequest(int ID, String ownerName, String ownerAddress, String phoneNumber, String model, String date, String problemDescription){
        this.ID = ID;
        this.ownerName = ownerName;
        this.ownerAddress = ownerAddress;
        this.phoneNumber = phoneNumber;
        this.model = model;
        this.date =date;
        this.problemDescription = problemDescription;
        status= ro.mpp2024.temalab4.model.RequestStatus.New;
    }

    public ComputerRepairRequest( String ownerName, String ownerAddress, String phoneNumber, String model, String date, String problemDescription){
        this.ownerName = ownerName;
        this.ownerAddress = ownerAddress;
        this.phoneNumber = phoneNumber;
        this.model = model;
        this.date =date;
        this.problemDescription = problemDescription;
        status= ro.mpp2024.temalab4.model.RequestStatus.New;
    }
//+ getters and setters
//+toString

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerAddress() {
        return ownerAddress;
    }

    public void setOwnerAddress(String ownerAddress) {
        this.ownerAddress = ownerAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }

    public Integer getID(){
        return ID;
    }
    public void setID(Integer id){
        ID = id;
    }

    public ro.mpp2024.temalab4.model.RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return
                "ID=" + ID +
                ", ownerName='" + ownerName + '\'' +
                ", model='" + model + '\'' +
                ", date='" + date + '\'' +
                ", problemDescription='" + problemDescription + '\''+
                ", status='" + status + '\'';

    }
}
