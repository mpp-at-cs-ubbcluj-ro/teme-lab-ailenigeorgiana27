package ro.mpp2024.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ro.mpp2024.domain.Inscriere;
import ro.mpp2024.domain.Participant;
import ro.mpp2024.domain.Proba;
import ro.mpp2024.repo.EntityRepoException;
import ro.mpp2024.service.Service;

import java.io.IOException;
import java.util.*;

public class MainController {

    private Service service;
    private Proba selectedProba = null;

    @FXML private ListView<Proba> probaListView;
    @FXML private TableView<Participant> participantTable;
    @FXML private TableColumn<Participant, String> participantNameColumn;
    @FXML private TableColumn<Participant, Integer> participantAgeColumn, nrProbeColumn;
    @FXML private TextField participantNameField, participantAgeField;
    @FXML private Label messageLabel;
    @FXML
    private TextField distanceField, stilField;

    private ObservableList<Proba> modelList = FXCollections.observableArrayList();
    private ObservableList<Participant> modelTable = FXCollections.observableArrayList();

    public void setService(Service service) {
        this.service = service;
        loadData();
    }

    private void loadData() {
        try {
            List<Proba> probe = service.findAllProbe();
            modelList.setAll(probe);
            probaListView.setItems(modelList);
            probaListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                selectedProba = newVal;
                if (newVal != null) {
                    loadParticipantsForProba(newVal);
                }
            });
        } catch (Exception e) {
            messageLabel.setText("Error loading data: " + e.getMessage());
        }
    }

    private List<Participant> getParticipantsForProbe(List<Proba> probes) throws EntityRepoException {
        List<Participant> participants = new ArrayList<>();
        for (Proba proba : probes) {
            List<Inscriere> inscriereList = service.getInscrierePentruProba(proba);
            for (Inscriere inscriere : inscriereList) {
                participants.add(inscriere.getParticipant());
            }
        }
        return participants;
    }

    private void loadParticipantsForProba(Proba proba) {
        try {
            List<Inscriere> inscriereList = service.getInscrierePentruProba(proba);
            List<Participant> participants = new ArrayList<>();
            for (Inscriere inscriere : inscriereList) {
                participants.add(inscriere.getParticipant());
            }

            System.out.println("DEBUG: Proba " + proba.getId() + " are " + participants.size() + " participanți.");

            modelTable.setAll(participants);
            participantTable.setItems(modelTable);
            participantTable.refresh();
        } catch (Exception e) {
            messageLabel.setText("Error loading participants: " + e.getMessage());
        }
    }

    @FXML
    private void initialize() {
        participantNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        participantAgeColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        nrProbeColumn.setCellValueFactory(cellData -> {
            try {
                Participant participant = cellData.getValue();
                int count = service.countProbeForParticipant(participant);
                return new SimpleIntegerProperty(count).asObject();
            } catch (Exception e) {
                return new SimpleIntegerProperty(0).asObject();
            }
        });
    }

    @FXML
    public void onBtnSearchClicked() {
        String distanceText = distanceField.getText();
        String stilText = stilField.getText();

        if (distanceText.isEmpty() || stilText.isEmpty()) {
            messageLabel.setText("Please fill in both distance and stil fields");
            return;
        }

        try {
            // Filtrăm probele pe baza distanței și stilului
            List<Proba> filteredProbes = service.findByDistanceAndStil(distanceText, stilText);

            if (filteredProbes.isEmpty()) {
                messageLabel.setText("No matching probes found");
                return;
            }

            // Încărcăm participanții pentru probele găsite
            List<Participant> participants = new ArrayList<>();
            for (Proba proba : filteredProbes) {
                List<Inscriere> inscriereList = service.getInscrierePentruProba(proba);
                for (Inscriere inscriere : inscriereList) {
                    participants.add(inscriere.getParticipant());
                }
            }

            // Actualizăm modelul și tabelul cu participanții
            modelTable.setAll(participants);
            participantTable.setItems(modelTable);

            participantTable.refresh();

            messageLabel.setText("Participants found for the selected probes");

        } catch (Exception e) {
            messageLabel.setText("Error during search: " + e.getMessage());
        }
    }


    @FXML
    public void onBtnInscriereClicked() {
        try {
            if (selectedProba == null || participantNameField.getText().isEmpty() || participantAgeField.getText().isEmpty()) {
                messageLabel.setText("Please complete all fields!");
                return;
            }

            String participantName = participantNameField.getText();
            int participantAge = Integer.parseInt(participantAgeField.getText());

            Participant participant = service.findByNameAge(participantName, participantAge);
            if (participant == null) {
                participant = new Participant(participantName, participantAge);
                participant = service.saveParticipant(participant);
            }

            Inscriere inscriere = new Inscriere(participant, selectedProba);
            Optional<Inscriere> savedInscriere = service.saveInscriere(inscriere);

            refreshAllData();
        } catch (NumberFormatException e) {
            messageLabel.setText("Age must be a number!");
        } catch (Exception e) {
            messageLabel.setText("Error during inscription: " + e.getMessage());
        }
    }

    private void refreshAllData() {
        try {
            List<Proba> updatedProbe = service.findAllProbe();
            Map<Proba, Integer> counts = new HashMap<>();
            for (Inscriere i : service.findAllInscrieri()) {
                counts.merge(i.getProba(), 1, Integer::sum);
            }
            modelList.setAll(updatedProbe);
            probaListView.setItems(modelList);

            if (selectedProba != null) {
                loadParticipantsForProba(selectedProba);
            }

            for (Proba proba : updatedProbe) {
                int numarParticipanti = counts.getOrDefault(proba, 0);
                System.out.println("Proba " + proba.getDistance() + " are " + numarParticipanti + " participanți.");
            }
            participantNameField.clear();
            participantAgeField.clear();
        } catch (Exception e) {
            messageLabel.setText("Error reloading data: " + e.getMessage());
        }
    }

    private void showError(String message) {
        messageLabel.setText(message);
    }


    public void onBtnLogOutClicked() throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/loginView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) messageLabel.getScene().getWindow();
            stage.setScene(scene);

            LoginViewController controller = fxmlLoader.getController();
            controller.setService(service);
            stage.show();
        }
    }
