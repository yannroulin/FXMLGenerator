package app.worker;

import app.beans.Selection;
import app.exceptions.MyFileException;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import javafx.collections.ObservableList;

/**
 * Implémentation de la couche "métier" de l'application.
 *
 * @author pa
 */
public class Worker implements WorkerItf {

    private final WorkerFile wrk;
    //Constante représentant le nombre max d'onglet de Beans présents dans la vue principale générée
    public static final int MAX_PANE_NUMBER = 10;
    //Constante provisoire permettant d'esquiver le problème de récupération de données
    public static final String PATH_TO_MODEL = ".\\src\\app\\models\\FormView.fxml";
    //Constante provisoire permettant d'esquiver le problème de récupération de données
    public static final String PATH_TO_CTRL = ".\\src\\app\\utils\\CtrlFormModel.java";
    //Constante contenant le chemin de fichier vers le modèle MainView.fxml
    public static final String PATH_TO_MAINVIEW = ".\\src\\app\\utils\\MainView.fxml";
    //Constante contenant le chemin de fichier vers le modèle MaiCtrl.fxml
    public static final String PATH_TO_MAINCTRL = ".\\src\\app\\utils\\MainCtrl.java";

    /**
     * Constructeur du worker, initiliase les attributs
     */
    public Worker() {
        wrk = new WorkerFile();
    }

    /**
     * Crée des Beans(Selection) avec les fichiers sélectionnés par
     * l'utilisateur dans le tableau
     *
     * @param beansDirectory Répertoire contenant les beans
     * @return ArrayList<Selection> contenant toutes les Selection
     * @throws MyFileException Remonte les exceptions si générées
     */
    @Override
    public ArrayList<Selection> createSelection(File beansDirectory) throws MyFileException {

        ArrayList<Selection> beansList = new ArrayList<>();
        ArrayList<File> beanName;

        try {
            beanName = wrk.searchBeans(beansDirectory);
            for (File bean : beanName) {
                Selection s = new Selection(bean.getName(), bean.getPath());
                beansList.add(s);
            }
        } catch (Exception ex) {
            throw new MyFileException("Worker.createSelection\n" + "Le répertoire que vous avez séléctionné ne contient pas de beans !", false);
        }
        return beansList;
    }

    /**
     * Demande au worker de générer les fichiers java et fxml avec les
     * informations reçues par le WorkerFile via la méthode permettant de lire
     * les fichiers
     *
     * @param beansSelectList Liste contenant les Selection
     * @throws MyFileException MyFileException Remonte les exceptions si
     * générées
     */
    @Override
    public void getAttributesofBeans(ObservableList<Selection> beansSelectList) throws MyFileException {

        String filePath;
        int compteur = 0;

        for (Selection beanInfo : beansSelectList) {
            compteur++;

            //Limite le traitement de beans Selection à 10
            if (compteur <= MAX_PANE_NUMBER) {
                filePath = beanInfo.getPath();
                byte[] tab;

                ArrayList<String> attributes = new ArrayList<>();
                //Demande au WorkerFile de lire les fichiers correspondant 
                //aux Selection
                List<String> lines = wrk.readFiles(filePath);

                for (String line : lines) {
                    //Retrouve les attributs présents dans les fichiers
                    if (line.contains("private")) {
                        attributes.add(line);
                    }
                }
                //Appel la méthode permettant de générer les FXML
                prepareFxml(attributes, beanInfo, PATH_TO_MODEL);
                //Appel la méthode permettant de générer les contrôleurs de vue
                prepareCtrl(attributes, beanInfo, beansSelectList, PATH_TO_CTRL);
                //Appel la méthode permettant de générer le MainView
                prepareMainView(beansSelectList, PATH_TO_MAINVIEW);
                //Appel la méthode permettant de générer le contrôleur de la MainView
                prepareMainCtrl(PATH_TO_MAINCTRL, beanInfo);
            }
        }
    }

    /**
     * Prépare le nouveau fichier FXML avec les attributs reçu en paramètres
     * ainsi que le modèle FXML par défaut. Demande au WorkerFile d'écrire ce
     * fichier
     *
     * @param attributesList Liste contenant les Attributs des beans
     * @param bean Bean Selection
     * @param pathToModel Chemin de fichier vers le modèle FXML par défaut
     * @throws MyFileException Remonte les exceptions si générées
     */
    private void prepareFxml(ArrayList<String> attributesList, Selection bean, String pathToModel) throws MyFileException {
        String xmlFileForm = "";
        String xmlFileList = "";
        String rowConstraints = "";
        String content = "";
        String[] tab;
        int rowIndex = -1;
        byte[] bytes = null;

        //Récupère les lignes du modèle FXML par défaut
        List<String> linesOfFxmlFile = wrk.readFiles(pathToModel);

        for (String attributes : attributesList) {
            //Sépare le tableau d'attributs pour pouvoir gérer les types
            tab = attributes.split("\\s+");
            //Enlève le ";" afin de l'ajouter dans les balises
            String attributName = tab[3].replace(";", "");

            //Sépare les balises à ajouter en fonction du type de l'attribut
            switch (tab[2]) {
                case "String":
                    rowIndex++;
                    //Ajoute une "mise en forme" de ligne
                    rowConstraints += "<RowConstraints minHeight=\"10.0\" prefHeight=\"30.0\" vgrow=\"SOMETIMES\" />";
                    //Créé le String contenant les nouvelles balises
                    xmlFileForm += "<Label fx:id=\"" + "lbl" + attributName + "\" prefHeight=\"17.0\" prefWidth=\"122.0\" text=\"" + attributName + "\" GridPane.rowIndex=\"" + rowIndex + "\"> "
                            + "<GridPane.margin>\n <Insets bottom=\"20.0\" left=\"10.0\" right=\"10.0\" top=\"20.0\" />\n</GridPane.margin> </Label>\n"
                            + "<TextField fx:id=\"" + "txt" + attributName + "\" prefHeight=\"25.0\" prefWidth=\"193.0\" GridPane.columnIndex=\"1\" GridPane.rowIndex=\"" + rowIndex + "\" > \n"
                            + "<GridPane.margin>\n <Insets left=\"20.0\" right=\"20.0\"/>\n </GridPane.margin>\n </TextField>";
                    break;
                case "int":
                    rowIndex++;
                    //Ajoute une "mise en forme" de ligne
                    rowConstraints += "<RowConstraints minHeight=\"10.0\" prefHeight=\"30.0\" vgrow=\"SOMETIMES\" />";
                    //Créé le String contenant les nouvelles balises
                    xmlFileForm += "<Label fx:id=\"" + "lbl" + attributName + "\" prefHeight=\"17.0\" prefWidth=\"122.0\" text=\"" + attributName + "\" GridPane.rowIndex=\"" + rowIndex + "\"> "
                            + "<GridPane.margin>\n <Insets bottom=\"20.0\" left=\"10.0\" right=\"10.0\" top=\"20.0\" />\n</GridPane.margin> </Label>\n"
                            + "<TextField fx:id=\"" + "txt" + attributName + "\" prefHeight=\"25.0\" prefWidth=\"193.0\" GridPane.columnIndex=\"1\" GridPane.rowIndex=\"" + rowIndex + "\" > \n"
                            + "<GridPane.margin>\n <Insets left=\"20.0\" right=\"20.0\"/>\n </GridPane.margin>\n </TextField>";
                    break;
                case "Date":
                    rowIndex++;
                    //Ajoute une "mise en forme" de ligne
                    rowConstraints += "<RowConstraints minHeight=\"10.0\" prefHeight=\"30.0\" vgrow=\"SOMETIMES\" />";
                    //Créé le String contenant les nouvelles balises
                    xmlFileForm += "<Label fx:id=\"" + "lbl" + attributName + "\" prefHeight=\"17.0\" prefWidth=\"122.0\" text=\"" + attributName + "\" GridPane.rowIndex=\"" + rowIndex + "\"> "
                            + "<GridPane.margin>\n <Insets bottom=\"20.0\" left=\"10.0\" right=\"10.0\" top=\"20.0\" />\n</GridPane.margin> </Label>\n"
                            + "<DatePicker fx:id=\"" + "dp" + attributName + "\" prefHeight=\"25.0\" prefWidth=\"455.0\" GridPane.columnIndex=\"1\" GridPane.rowIndex=\"" + rowIndex + "\"><GridPane.margin>"
                            + "<Insets left=\"20.0\" right=\"20.0\" /></GridPane.margin></DatePicker>";
                    break;
                default:
            }
        }
        //Création du Path du répertoire dans lequel sera écrit le fichier
        String destinationFolder = bean.getPath().replace(bean.getBean(), "");
        destinationFolder += "..\\presentation\\";
        String fileName = bean.getBean().replace(".java", "View.fxml");
        Path path = Paths.get(destinationFolder + fileName);

        //Création du nom du fichier permettant de faire le lien avec le Ctrl
        String ctrlLink = bean.getBean().replace(".java", "Ctrl");

        //Parcours les lignes du modèles FXML par défaut
        for (String line : linesOfFxmlFile) {
            //Recherche le commentaire
            if (line.contains("<!--FXML Generator - insert here -->")) {
                //Ajoute les nouvelles balises après le commentaire
                content += "\n" + xmlFileForm;
                //Recherche le commentaire
            } else if (line.contains("<!--FXML Generator - insert row -->")) {
                //Ajoute les contarintes pour chaque ligne
                content += rowConstraints;
            } else if (line.contains("fx:controller=\"\"")) {
                //Indique le lien vers le contrôleur de vue
                content += "<BorderPane maxHeight=\"-Infinity\" maxWidth=\"-Infinity\" minHeight=\"-Infinity\" minWidth=\"-Infinity\" xmlns=\"http://javafx.com/javafx/11.0.1\" xmlns:fx=\"http://javafx.com/fxml/1\" fx:controller=\"app.presentation." + ctrlLink + "\">\n";
            } else {
                content += line;
            }
            bytes = content.getBytes();
        }
        //Demande au WorkerFile d'écrire le fichier
        wrk.writeFile(path, bytes);
    }

    /**
     * Prépare le nouveau fichier Ctrl avec les attributs reçu en paramètres
     * ainsi que le modèle de contrôleur par défaut. Demande au WorkerFile
     * d'écrire ce fichier
     *
     * @param attributesList Liste contenant les Attributs des beans
     * @param bean Selection Bean
     * @param beansSelectedList List de tous les beans sélectionné par
     * l'utilisateur
     * @param pathToCtrl Chemin de fichier vers le modèle de contrôleur par
     * défaut
     * @throws MyFileException Remonte les exceptions si générées
     */
    private void prepareCtrl(ArrayList<String> attributesList, Selection bean, ObservableList<Selection> beansSelectedList, String pathToCtrl) throws MyFileException {
        String[] tab;
        byte[] bytes = null;
        String linesToAdd = "";
        String content = "";
        int compteur = 0;

        //Récupère les lignes du modèle FXML par défaut
        List<String> linesOfCtrlFile = wrk.readFiles(pathToCtrl);

        //Créé le String contenant les attributs à ajouter au nouveau fichier
        for (String attributes : attributesList) {
            linesToAdd += "@FXML\n" + attributes;
        }

        //Création du Path du répertoire dans lequel sera écrit le fichier
        String destinationFolder = bean.getPath().replace(bean.getBean(), "");
        destinationFolder += "..\\presentation\\";
        String fileName = bean.getBean().replace(".java", "Ctrl.java");
        Path path = Paths.get(destinationFolder + fileName);

        //Nom de la classe
        String className = fileName.replace(".java", "");

        //Parcours les lignes du modèle Ctrl par défaut
        for (String line : linesOfCtrlFile) {
            //Recherche le commentaire
            if (line.contains("//FXML Generator - insert here")) {
                //Ajoute le code correspondant aux attributs
                content += "\n" + linesToAdd;
            } else if (line.contains("//FXML Generator - insert import")) {

                //Parcours les dossiers afin de créer les imports
                for (Selection file : beansSelectedList) {
                    content += "import app.beans." + file.getBean().replace(".java", "") + ";";
                }

            } else if (line.contains("public class CtrlFormModel implements Initializable {")) {
                //Donne un nom de classe correspondant au nom du fichier
                content += "public class " + className + " implements Initializable {";
            } else {
                content += line;
            }
            bytes = content.getBytes();
        }
        //Demande au WorkerFile d'écrire le fichier
        wrk.writeFile(path, bytes);
    }

    /**
     * Prépare le MainView.fxml qui contiendra les onglets correspondant aux
     * autres fichiers FXML. Demande au WorkerFile d'écrire le fichier
     *
     * @param listBeans Liste des beans séléctionnés
     * @param path Chemin de fichier du modèle par défaut MainView
     * @throws MyFileException Remonte les exceptions si générées
     */
    private void prepareMainView(ObservableList<Selection> listBeans, String path) throws MyFileException {
        String linesToAdd = "";
        String content = "";
        String destinationFolder = "";
        byte[] bytes = null;
        String[] tab;

        //Récupère les lignes du MainView.fxml par défaut
        List<String> mainFxmlContent = wrk.readFiles(path);

        //Parcours les beans sélectionnés par l'utilisateur
        for (Selection beanSelect : listBeans) {
            String fxmlFileName = beanSelect.getBean().replace(".java", "View.fxml");
            String ids = beanSelect.getBean().replace(".java", "");

            //Ajoute les balises permettant de créer un onglet et de faire le lien avec les autres modèles FXML
            linesToAdd += "<Tab fx:id=\"tab" + ids + "\" text=\"" + ids + "\">\n<content>\n<fx:include fx:id=\"id" + ids + "\" source=\"" + fxmlFileName + "\" /> \n </content>\n</Tab>";

            //Récupère le chemin de fichier du bean
            destinationFolder = beanSelect.getPath().replace(beanSelect.getBean(), "");
        }

        //Transforme ce chemin de fichier afin de stocker le fichier dans "app.presentation"
        destinationFolder += "..\\presentation\\MainView.fxml";
        Path pathFinalDirectory = Paths.get(destinationFolder);

        //Parcours les lignes du MainView.fxmlpar défaut
        for (String line : mainFxmlContent) {
            //Recherche le commentaire
            if (line.contains(" <!--FXML Generator - insert here -->")) {
                //Ajoute les nouvelles balises après le commentaire
                content += "\n" + linesToAdd;
            } else {
                content += line;
            }
            bytes = content.getBytes();
        }
        //Demande au WorkerFile d'écrire le fichier
        wrk.writeFile(pathFinalDirectory, bytes);
    }

    /**
     * * Prépare le MainCtrl.java permettant de gérer le MainView.fxml. Demande
     * au WorkerFile d'écrire le fichier
     *
     * @param pathofCtrl Chemin de fichier du modèle MainCtrl par défaut
     * @param bean Bean Selection
     * @throws MyFileException Remonte les exceptions si générées
     */
    private void prepareMainCtrl(String pathofCtrl, Selection bean) throws MyFileException {
        String content = "";
        byte[] bytes = null;

        //Récupère les lignes du modèle MainCtrl par défaut
        List<String> mainCtrlContent = wrk.readFiles(pathofCtrl);

        for (String line : mainCtrlContent) {
            //Recherche le commentaire
            if (line.contains("/* FXML Generator - insert package*/")) {
                //Ajoute le bon package
                content += "package app.presentation;";
            }
            content += line;
        }

        //Prèpare le chemin de fichier de destination du fichier
        String destinationFolder = bean.getPath().replace(bean.getBean(), "");
        destinationFolder += "..\\presentation\\MainCtrl.java";
        Path pathFinalDirectory = Paths.get(destinationFolder);

        bytes = content.getBytes();

        //Demande au WorkerFile d'écrire le fichier
        wrk.writeFile(pathFinalDirectory, bytes);
    }

    /**
     * Méthode faisant le lien entre le MainCtrl et le WorkerFile
     *
     * @param beansDirectory Répertoire dans lequel chercher les beans
     * @return ArrayList<File> contenant les beans
     */
    @Override
    public ArrayList<File> searchBeans(File beansDirectory) {
        return wrk.searchBeans(beansDirectory);
    }

    /**
     * Méthode faisant le lien entre le MainCtrl et le WorkerFile
     *
     * @return ArrayList<String> contenantles modèles
     */
    @Override
    public ArrayList<String> searchModels() {
        return wrk.searchModels();
    }
}
