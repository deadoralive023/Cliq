package edu.cliq;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Button importFile;
    private final FileChooser chooser = new FileChooser();
    public static String filePath;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        importFile.setOnAction(event -> {
            try {
                handleButtonAction(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void handleButtonAction(ActionEvent event) throws IOException {
        File selectedFile = chooser.showOpenDialog(null);
        if(selectedFile != null){
            filePath = selectedFile.getAbsolutePath();
            Parent window = FXMLLoader.load(getClass().getResource("/edu.cliq/dataController.fxml"));
            Scene newScene;
            newScene = new Scene(window);
            Stage mainWindow;
            mainWindow = (Stage)  ((Node)event.getSource()).getScene().getWindow();
            mainWindow.setScene(newScene);
        }

    }
}
