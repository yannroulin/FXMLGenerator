/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.presentation;

import app.beans.Selection;
import app.helpers.JfxPopup;
import app.worker.Worker;
import app.worker.WorkerItf;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;

/**
 * FXML Controller class
 *
 * @author RoulinY01
 */
public class MainCtrl implements Initializable {

    private static final String IMAGES_FOLDER = "app/img/";
    public static final String DEFAULT_BEANS_PATH = "\\src\\app\\beans";

    private WorkerItf wrk;
    @FXML
    private ImageView imgLogo;
    @FXML
    private Button btnExplorer;
    @FXML
    private Label lblProjectName;
    @FXML
    private Button btnGenerateViews;
    @FXML
    private TableView<Selection> tableChoose;
    @FXML
    private TableColumn<Selection, String> beansColumn;
    @FXML
    private TableColumn<Selection, String> modelsColumn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        wrk = new Worker();
        Image img = new Image(IMAGES_FOLDER + "logo.png");
        imgLogo.setImage(img);
        beansColumn.setCellValueFactory(new PropertyValueFactory<Selection, String>("bean"));
        modelsColumn.setCellValueFactory(new PropertyValueFactory<Selection, String>("modeles"));
    }

    @FXML
    private void openExplorer(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select your java application folder");
        chooser.setInitialDirectory(new File("."));
        File externApplicationDirectory = chooser.showDialog(lblProjectName.getScene().getWindow());
        File beansDirectory = new File(externApplicationDirectory.getAbsolutePath() + DEFAULT_BEANS_PATH);

        ArrayList<Selection> beansInFolder = wrk.createSelection(beansDirectory);

        tableChoose.setItems(FXCollections.observableList(beansInFolder));
        ArrayList<String> modelsList = searchModels();
        modelsColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(modelsList)));
        modelsColumn.setEditable(true);
    }

    @FXML
    private void generateViews(ActionEvent event) {
    }

    private ArrayList<String> searchModels() {

        return wrk.searchModels();
    }

    private ArrayList<String> searchBeans(File beansDirectory) {
        return wrk.searchBeans(beansDirectory);
    }
}
