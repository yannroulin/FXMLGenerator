package app;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author RoulinY01
 */
public class MainCtrl implements Initializable {

    private static final String IMAGES_FOLDER = "app/img/";

    @FXML
    private ImageView imgLogo;
    @FXML
    private Button btnExplorer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image img = new Image(IMAGES_FOLDER + "logo.png");
        imgLogo.setImage(img);
    }

    @FXML
    private void openExplorer(ActionEvent event) {
    }

}
