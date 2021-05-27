package app;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import app.helpers.JfxPopup;
import app.presentation.MainCtrl;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author RoulinY01
 */
public class App extends Application {

    private static final String IMAGES_FOLDER = "app/img/";
    private static final String TITLE = "FXML Generator";
    private static final String LOGO = "app/img/icon.png";
    private static final String FXML = "/app/presentation/MainView.fxml";
    private static final String ERROR_TITLE = "Erreur";
    private static final String ERROR_MSG = "App.start:\nProblème avec le fichier";

    @Override
    public void start(Stage stage) throws Exception {

        // charger la vue principale
        FXMLLoader loader = null;
        Parent mainView = null;
        try {
            loader = new FXMLLoader(getClass().getResource(FXML));
            mainView = loader.load();
        } catch (java.lang.IllegalStateException | IOException ex) {
            String errMsg = ERROR_MSG + " " + FXML + ".\n\n" + ex.getMessage();
            JfxPopup.displayError(ERROR_TITLE, null, errMsg);
            System.exit(-1);
        }

        // récupérer une référence sur son contrôleur
        MainCtrl mainCtrl = loader.getController();

        // préparer la première scène
        Scene scene1 = new Scene(mainView);

        // modifier l'estrade pour la première scène
        stage.setScene(scene1);

        // choisir un titre pour la fenêtre principale (pour la pièce de théâtre)
        stage.setTitle(TITLE);

        // rajouter une icône dans la barre de titre de la vue principale
        stage.getIcons().add(new Image(LOGO));
        
        //Empêche l'utilisateur de redimensionner l'application
        stage.setResizable(false);

        // afficher cette première vue (tirer le rideau)
        stage.show();

        // ajout d'un écouteur pour contrôler la sortie de l'application.
        stage.setOnCloseRequest(e -> {

            // pour éviter que la fenêtre principale ne se ferme dans tous les cas
            e.consume();

            // lors d'une demande de sortie, laisser le travail à faire au contrôleur
            mainCtrl.quitter();
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
