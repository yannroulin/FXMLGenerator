/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.worker;

import app.exceptions.MyFileException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author RoulinY01
 */
public class WorkerFile {

    //Constante contenant le répertoire des modèles
    public static final String DEFAULT_FXML_PATH = "\\src\\app\\models\\";

    /**
     * Lis le fichier via le chemin reçu en paramètre
     *
     * @param path CHemin du fichier à lire
     * @return List<String> contenant les lignes du fichier lu
     * @throws MyFileException Remonte les exceptions si générées
     */
    public List<String> readFiles(String path) throws MyFileException {
        byte[] bytesTab;
        List<String> lines;

        Path finalPath = Paths.get(path);

        try {
            //Lis le fichier et l'ajoute dans une variable
            bytesTab = Files.readAllBytes(finalPath);
            lines = Files.readAllLines(finalPath, Charset.forName("UTF-8"));

        } catch (IOException ex) {
            throw new MyFileException("Worker.readFxml\n" + "Lecture de fichier impossible", false);
        }
        return lines;
    }

    /**
     * Cherche les beans dans le répertoire reçu en paramètre
     *
     * @param beansDirectory Répertoire dans lequel chercher
     * @return ArrayList<File> contenant les beans trouvés
     */
    public ArrayList<File> searchBeans(File beansDirectory) {
        ArrayList<File> beansList = new ArrayList<>();

        //Liste les fichiers du répertoire
        File[] flist = beansDirectory.listFiles();
        for (File file : flist) {
            beansList.add(file);
        }
        return beansList;
    }

    /**
     * Recherche les modèles en fonction de la constante
     *
     * @return ArrayList<String> contenant les modèles trouvés
     */
    public ArrayList<String> searchModels() {
        File modelsDirectory = new File("." + DEFAULT_FXML_PATH);
        File[] tableModels = modelsDirectory.listFiles();
        ArrayList<String> models = new ArrayList<>();

        for (File model : tableModels) {
            models.add(model.getName());
        }
        return models;
    }

    /**
     * Ecrit un fichier avec le tableau de bytes reçu en paramètres ainsi que le
     * chemin de fichier.
     *
     * @param path Destination de l'écriture du fichier
     * @param bytes Tableau de bytes contenant le contenu du fichier écrire
     * @throws MyFileException Remonte les exceptions si générées
     */
    public void writeFile(Path path, byte[] bytes) throws MyFileException {
        try {
            Files.write(path, bytes);
        } catch (IOException ex) {
            throw new MyFileException("Worker.writeFxml\n" + "Erreur dans la génération de votre vue !", false);
        }
    }

}
