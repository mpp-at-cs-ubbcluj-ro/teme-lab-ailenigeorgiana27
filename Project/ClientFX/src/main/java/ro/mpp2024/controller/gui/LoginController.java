package ro.mpp2024.controller.gui;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ro.mpp2024.domain.PersoanaOficiu;
import ro.mpp2024.services.AppException;
import ro.mpp2024.services.IServices;

public class LoginController {
    IServices server;
    ListeProbeController controller;

    @FXML
    TextField txtUsername;
    @FXML
    TextField txtPassword;
    @FXML
    Button button;
    Parent mainChatParent;

    public void setParent(Parent p) {
        mainChatParent = p;
    }

    private PersoanaOficiu crtUser;

    public void setService(IServices service) {
        this.server = service;
    }


    public void eventHandler(MouseEvent mouseEvent) {

        String nume = txtUsername.getText();
        String passwd = txtPassword.getText();
        crtUser = new PersoanaOficiu(nume, passwd);


        try {
            server.login(crtUser, controller);
            // Util.writeLog("User succesfully logged in "+crtUser.getId());
            Stage stage = new Stage();
            stage.setTitle("Window for " + crtUser.getUsername());
            stage.setScene(new Scene(mainChatParent));

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    controller.logout();
                    System.exit(0);
                }
            });

            stage.show();
            controller.setUser(crtUser);
            controller.initModelProbe();
            ((Node) (mouseEvent.getSource())).getScene().getWindow().hide();

        } catch (AppException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("MPP chat");
            alert.setHeaderText("Authentication failure");
            alert.setContentText("Wrong username or password");
            alert.showAndWait();
        }
    }

    public void setListeProbeController(ListeProbeController chatCtrl) {
        this.controller = chatCtrl;
    }
}