package app;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("presentation/MainView.fxml"));

        Scene scene = new Scene(root);

        Image img = new Image(IMAGES_FOLDER + "icon.png");

        stage.setScene(scene);
        //IMPORTANT !!!!
        stage.setResizable(false);
        stage.getIcons().add(img);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
