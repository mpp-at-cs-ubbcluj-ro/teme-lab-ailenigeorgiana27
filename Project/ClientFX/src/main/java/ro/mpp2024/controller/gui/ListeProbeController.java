package ro.mpp2024.controller.gui;

import javafx.concurrent.Task;
import ro.mpp2024.controller.gui.InscriereController;
import ro.mpp2024.domain.Participant;
import ro.mpp2024.domain.PersoanaOficiu;
import ro.mpp2024.domain.Proba;
import ro.mpp2024.domain.enums.Stil;
import ro.mpp2024.services.AppException;
import ro.mpp2024.services.IObserver;
import ro.mpp2024.services.IServices;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ListeProbeController implements Initializable, IObserver {
    IServices server;

    Stage stageInscriere;
    ObservableList<Proba> modelProbe = FXCollections.observableArrayList();
    ObservableList<Participant> modelParticipanti = FXCollections.observableArrayList();
    private PersoanaOficiu user;

    @FXML
    Button button;
    @FXML
    TableView<Proba> tableViewProbe;
    @FXML
    TableView<Participant> tableViewParticipanti;
    @FXML
    TableColumn<Proba, Integer> tableColumnId;
    @FXML
    TableColumn<Proba, Integer> tableColumnNumarParticipanti; // Adăugăm o coloană pentru numărul de participanți
    @FXML
    TableColumn<Proba, Float> tableColumnDistanta;
    @FXML
    TableColumn<Proba, Stil> tableColumnStil;
    @FXML
    TableColumn<Participant, String> tableColumnNume;
    @FXML
    TableColumn<Participant, Integer> tableColumnVarsta;
    @FXML
    TableColumn<Participant, Integer> tableColumnNrProbe;


    boolean probaClicked = false;
    Proba lastClicked;

    public void setServer(IServices service) throws AppException {
        this.server = service;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Your initialization code here
        tableColumnId.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getId()));
        tableColumnDistanta.setCellValueFactory(new PropertyValueFactory<Proba, Float>("distanta"));
        tableColumnStil.setCellValueFactory(new PropertyValueFactory<Proba, Stil>("stil"));

        // Set the column for the number of participants
        tableColumnNumarParticipanti.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getNrParticipants()));

        // Set the items for the probe table
        tableViewProbe.setItems(modelProbe);
    }


    public void initModelProbe() throws AppException {
        // Run the task in the background
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Proba[] probas = server.getProbe();
                List<Proba> list = new ArrayList<>();
                for (Proba p : probas) {
                    // Set the number of participants for each probe
                    Participant[] participants = server.getParticipantiDupaProba(p);
                    p.setNrParticipants(participants.length);  // Set the number of participants
                    list.add(p);
                }
                // Update the model on the JavaFX thread
                Platform.runLater(() -> modelProbe.setAll(list));
                return null;
            }
        };
        new Thread(task).start();
    }




    public void handleProbaClicked(MouseEvent mouseEvent) throws AppException {
        probaClicked = true;
        Proba p = tableViewProbe.getSelectionModel().getSelectedItem();
        lastClicked = p;

        Participant[] participants = server.getParticipantiDupaProba(p);
        List<Participant> list = new ArrayList<>();
        for (int i = 0; i < participants.length; i++) {
            list.add(participants[i]);
        }
        modelParticipanti.setAll(list);

        tableColumnNume.setCellValueFactory(new PropertyValueFactory<Participant, String>("name"));
        tableColumnVarsta.setCellValueFactory(new PropertyValueFactory<Participant, Integer>("age"));
        // Setăm manual o celulă care să afișeze numărul de probe (folosim getProbeDupaParticipanti)
        tableColumnNrProbe.setCellValueFactory(param -> {
            try {
                return new SimpleObjectProperty<>(server.getProbeDupaParticipanti(param.getValue()).length);
            } catch (AppException e) {
                e.printStackTrace();
                return new SimpleObjectProperty<>(0);
            }
        });
        tableViewParticipanti.setItems(modelParticipanti);

        // Actualizează numărul de participanți la proba selectată
        p.setNrParticipants(participants.length);  // Asumăm că Proba are un setter pentru număr participanți
        tableViewProbe.refresh(); // Reîmprospătează tabelul
    }

    public void handleButton(MouseEvent mouseEvent) throws AppException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/Inscriere.fxml"));

        AnchorPane root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stageInscriere = new Stage();
        stageInscriere.setTitle("Inscriere");
        stageInscriere.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(root);
        stageInscriere.setScene(scene);

        InscriereController inscriereController = loader.getController();
        inscriereController.setService(server);

        // După înscriere, actualizează lista de probe
        initModelProbe(); // Reîncarcă toate probele pentru a reflecta noile înscriere
        stageInscriere.show();
    }


    public void handleLogout(MouseEvent mouseEvent) {
        logout();  // Execută logout-ul
        ((Node) (mouseEvent.getSource())).getScene().getWindow().hide();  // Ascunde fereastra curentă

        // Închide fereastra de înscriere dacă este deschisă
        if (stageInscriere != null) {
            stageInscriere.close();
        }

        // Închide aplicația complet (opțional, în funcție de nevoile tale)
        Platform.exit();
    }

    void logout() {
        try {
            // Asigură-te că utilizatorul este dezabonat de la server
            if (user != null) {
                server.logout(user, this);  // De exemplu, această linie trebuie să elimine observer-ul corect
            }
        } catch (AppException e) {
            System.out.println("Logout error: " + e);
        }
    }



    public void setUser(PersoanaOficiu crtUser) {
        this.user = crtUser;
    }

    @Override
    public void newInscriere() throws AppException {
        Platform.runLater(() -> {
            try {
                // Actualizează toate probele, nu doar pe cele selectate
                Proba[] probas = server.getProbe();  // Reîncarcă toate probele
                List<Proba> list = new ArrayList<>();
                for (Proba p : probas) {
                    // Obține lista de participanți pentru fiecare probă
                    Participant[] participants = server.getParticipantiDupaProba(p);
                    p.setNrParticipants(participants.length);  // Actualizează numărul de participanți
                    list.add(p);
                }
                // Reîncarcă modelul cu probele actualizate
                modelProbe.setAll(list);
                tableViewProbe.refresh();  // Reîmprospătează tabelul pentru a reflecta modificările
            } catch (AppException e) {
                e.printStackTrace();
            }
        });
    }


}


