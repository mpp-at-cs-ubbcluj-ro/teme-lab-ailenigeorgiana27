package ro.mpp2024.controller.gui;

import javafx.scene.control.Alert;
import ro.mpp2024.domain.Participant;
import ro.mpp2024.domain.enums.Stil;
import ro.mpp2024.services.AppException;
import ro.mpp2024.services.IServices;
import ro.mpp2024.domain.Proba;


import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.util.List;

public class InscriereController {
    IServices server;

    ObservableList<Proba> modelProbe = FXCollections.observableArrayList();
    ObservableList<Proba> modelProbeParticipant = FXCollections.observableArrayList();

    @FXML
    TextField txtNume;
    @FXML
    TextField txtVarsta;
    @FXML
    TableView<Proba> tableView;
    @FXML
    TableColumn<Proba,Integer> tableColumnId;
    @FXML
    TableColumn<Proba,Float> tableColumnDistanta;
    @FXML
    TableColumn<Proba, Stil> tableColumnStil;
    @FXML
    TableView<Proba> tableViewParticipant;
    @FXML
    TableColumn<Proba,Integer> tableColumnIdP;
    @FXML
    TableColumn<Proba,Float> tableColumnDistantaP;
    @FXML
    TableColumn<Proba, Stil> tableColumnStilP;

    public void setService(IServices service) throws AppException {
        this.server = service;
        initModelProbe();
    }

    @FXML
    public void initialize() {
        tableColumnId.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getId()));
        tableColumnDistanta.setCellValueFactory(new PropertyValueFactory<Proba, Float>("distanta"));
        tableColumnStil.setCellValueFactory(new PropertyValueFactory<Proba, Stil>("stil"));
        tableColumnIdP.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getId()));
        tableColumnDistantaP.setCellValueFactory(new PropertyValueFactory<Proba, Float>("distanta"));
        tableColumnStilP.setCellValueFactory(new PropertyValueFactory<Proba, Stil>("stil"));
        tableView.setItems(modelProbe);


    }

    private void initModelProbe() throws AppException {
        Proba[] probas = server.getProbe();
        modelProbe.setAll(probas);
    }

    public void handleAdd(MouseEvent mouseEvent) {
        Proba p = tableView.getSelectionModel().getSelectedItem();
        if(modelProbeParticipant.contains(p)){
            MessageAlert.showErrorMessage(null,"Nu poti adauga aceeasi proba de 2 ori");
        }
        else{
            modelProbeParticipant.add(p);
        }
        tableViewParticipant.setItems(modelProbeParticipant);

    }

    public void handleInscrie(MouseEvent mouseEvent) throws AppException {
        Participant participant = new Participant(txtNume.getText(),Integer.parseInt(txtVarsta.getText()));
        List<Proba> probe = modelProbeParticipant;
        Proba[] probas = new Proba[probe.size()];
        for(int i=0;i<probe.size();i++){
            probas[i]=probe.get(i);
        }
        server.Inscrie(participant,probas);
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Succes!",  "Inscrierea s-a realizat cu succes!");

//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getResource("/views/Update.fxml"));
//
//        AnchorPane root = null;
//        try {
//            root = loader.load();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Stage stage = new Stage();
//        stage.setTitle("Update");
//        stage.initModality(Modality.WINDOW_MODAL);
//        Scene scene = new Scene(root);
//        stage.setScene(scene);
//
//        UpdateContoller updateContoller = loader.getController();
//        updateContoller.setService(server);
//
//        stage.show();
    }

}
