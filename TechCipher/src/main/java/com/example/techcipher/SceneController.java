package com.example.techcipher;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class SceneController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private final FileChooser fileChooser = new FileChooser();

    ImageCipher imageCipher = new ImageCipher();
    AudioCipherEncrypt audioCipherEncrypt = new AudioCipherEncrypt();
    AudioCipherDecrypt audioCipherDecrypt = new AudioCipherDecrypt();
    @FXML
    private MenuItem imageEncryption;
    @FXML
    private TextArea imgInput;
    @FXML
    private TextArea imgOutput;
    @FXML
    private TextArea audInput;
    @FXML
    private TextArea audOutput;
    @FXML
    private Label WarningImage;
    @FXML
    private Label WarningAudio;
    @FXML
    public void onSceneSwitch(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("scene1.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void onSceneSwitch2(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("scene2.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void FileSelect(ActionEvent event) throws IOException {

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        File selectedFile;
        try {
            selectedFile = fileChooser.showOpenDialog(stage);
        }catch (Exception e){
            selectedFile = null;
            System.out.println("File not selected");

            WarningImage.setText("Файл не выбран!");
        }
        if (selectedFile != null) {
            WarningImage.setText("Зашифровано успешно!");
            imageCipher.encrypt(imgInput.getText(),selectedFile.toString());
        }

    }
    @FXML
    public void FileSelect2(ActionEvent event) throws IOException {

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        File selectedFile;
        try {
            selectedFile = fileChooser.showOpenDialog(stage);
        }catch (Exception e){
            System.out.println("File not selected");
            WarningImage.setText("Файл не выбран!");
            selectedFile = null;
        }

        if (selectedFile != null) {
            String[] decrypted = imageCipher.decrypt(selectedFile.toString());
            imgOutput.setText("");
            WarningImage.setText("Расшифровано успешно!");
            for (String s : decrypted) {
                imgOutput.setText(imgOutput.getText() + " " + s+"\n");
            }
        }
    }
    @FXML
    public void FileSelect3(ActionEvent event) throws IOException{

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        File selectedFile;
        try {
            System.out.println("ss");
            selectedFile = fileChooser.showOpenDialog(stage);
        }catch (Exception e){
            selectedFile = null;
            System.out.println("File not selected");

            WarningAudio.setText("Файл не выбран!");
        }
        if (selectedFile != null) {
            WarningAudio.setText("Зашифровано успешно!");

            audioCipherEncrypt.encrypt(selectedFile.toString(),audInput.getText());
        }

    }
    @FXML
    public void FileSelect4(ActionEvent event) throws IOException {

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        File selectedFile;
        try {
            selectedFile = fileChooser.showOpenDialog(stage);
        }catch (Exception e){
            System.out.println("File not selected");
            WarningAudio.setText("Файл не выбран!");
            selectedFile = null;
        }

        if (selectedFile != null) {
            String decrypted = audioCipherDecrypt.decrypt(selectedFile.toString());
            audOutput.setText(decrypted);
            WarningAudio.setText("Расшифровано успешно!");

        }
    }
}