/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.presentation;

import app.beans.Selection;
import app.exceptions.MyFileException;
import app.helpers.JfxPopup;
import app.worker.Worker;
import app.worker.WorkerItf;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;

/**
 * FXML Controller class
 *
 * @author RoulinY01
 */
public class MainCtrl implements Initializable {

    //Constante contenant le répertoire des images du projet (logo + icone)
    private static final String IMAGES_FOLDER = "app/img/";
    //Constante contenant le répertoire par défaut des Beans à l'EMF
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

    /**
     * Méthode exécutée au lancement de l'application
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        wrk = new Worker();
        Image img = new Image(IMAGES_FOLDER + "logo.png");
        imgLogo.setImage(img);
    }

    /**
     * Méthode exécutée une fois que l'utilisateur aura appuyé sur le bouton
     * "Select Java application directory"
     *
     * @param event évenement créé lors de la pression du bouton
     */
    @FXML
    private void openExplorer(ActionEvent event) {

        //Création de l'explorateur de fichier
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select your java application folder");
        chooser.setInitialDirectory(new File("."));
        //Récupération du chemin de fichier
        File externApplicationDirectory = chooser.showDialog(lblProjectName.getScene().getWindow());

        //Si l'utilisateur ferme l'explorateur
        if (externApplicationDirectory == null) {
            return;
        }
        //Création du répertoire par défaut des beans dans des projets Java de l'EMF
        File beansDirectory = new File(externApplicationDirectory.getAbsolutePath() + DEFAULT_BEANS_PATH);
        ArrayList<Selection> selectionInFolder = null;

        try {
            //Appel du Worker pour créer des beans à partir des fichiers
            selectionInFolder = wrk.createSelection(beansDirectory);

            ///Appel du Worker pour récupérer les modèles
            ArrayList<String> modelsList = searchModels();

            //Permission de la sélection multiple dans le tableau
            tableChoose.getSelectionModel().setSelectionMode(
                    SelectionMode.MULTIPLE
            );

            TableColumn<Selection, String> beansColumn = new TableColumn<>("Beans");
            beansColumn.prefWidthProperty().bind(tableChoose.widthProperty().multiply(0.5));
            beansColumn.setResizable(false);
            beansColumn.setCellValueFactory(cellData -> cellData.getValue().beanProperty());
            beansColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            tableChoose.getColumns().add(beansColumn);

            TableColumn<Selection, String> modelsColumn = new TableColumn<>("Models");
            modelsColumn.prefWidthProperty().bind(tableChoose.widthProperty().multiply(0.5));
            modelsColumn.setResizable(false);
            modelsColumn.setEditable(true);
            modelsColumn.setCellValueFactory(cellData -> cellData.getValue().modelProperty());
            modelsColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(modelsList)));
            tableChoose.getColumns().add(modelsColumn);

            tableChoose.getItems().addAll(selectionInFolder);

            //Bouton de l'explorateur de fichier caché
            btnExplorer.setVisible(false);
            //Affichage du label du nom de l'application et du bouton de génération
            btnGenerateViews.setVisible(true);
            lblProjectName.setVisible(true);
            lblProjectName.setText(externApplicationDirectory.getName());

        } catch (Exception ex) {
            JfxPopup.displayError("Erreur", "Pas de Beans dans le répertoire courant !", ex.getMessage());
        }
    }

    /**
     * Méthode exécutée lors de la pression de l'utilisateur sur le bouton
     * "Generate views"
     *
     * @param event évenement créé lors de la pression du bouton
     */
    @FXML
    private void generateViews(ActionEvent event) {

        tableChoose.getSelectionModel().selectAll();
        List<Selection> sel = tableChoose.getSelectionModel().getSelectedItems();
        
        for (Selection selection : sel) {
            System.out.println(selection.getBean() + " " + selection.getModel());
        }
        
        //Récupère les fichiers sélectionnés dans le tableau
        ObservableList<Selection> selected = tableChoose.getSelectionModel().getSelectedItems();

        try {
            //Appel du worker pour traiter les fichiers
            wrk.getAttributesofBeans(selected);
        } catch (MyFileException ex) {
            //Affichage d'une pop-up en cas de problèmes de génération de vue
            JfxPopup.displayError("Erreur", "Erreur dans la génération de vos vues !", ex.getMessage());
        }
    }

    /**
     * Demande au worker de rechercher les modèles FXML présents dans
     * l'application
     *
     * @return ArrayList<String> contenant les modèles
     */
    private ArrayList<String> searchModels() {
        return wrk.searchModels();
    }

    /**
     * Demande au worker de rechercher les beans présents dans l'application
     *
     * @return ArrayList<String> contenant les modèles
     */
    private ArrayList<File> searchBeans(File beansDirectory) throws MyFileException {
        return wrk.searchBeans(beansDirectory);
    }

    public void quitter() {
        // faire qq chose avant de quitter
        // wrk.fermerBD();
        // System.out.println("Je vous quitte !");

        // obligatoire pour bien terminer une application JavaFX
        Platform.exit();
    }

}
