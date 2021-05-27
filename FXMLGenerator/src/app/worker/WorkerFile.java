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

    public static final String DEFAULT_FXML_PATH = "\\src\\app\\modelsviews\\";

    public List<String> readFiles(String path) throws MyFileException {
        byte[] bytesTab;
        List<String> lines;

        Path finalPath = Paths.get(path);

        try {
            bytesTab = Files.readAllBytes(finalPath);
            lines = Files.readAllLines(finalPath, Charset.forName("UTF-8"));

        } catch (IOException ex) {
            throw new MyFileException("Worker.readFxml\n" + "Lecture de fichier impossible", false);
        }
        return lines;
    }

    public ArrayList<File> searchBeans(File beansDirectory) throws MyFileException {
        ArrayList<File> beansList = new ArrayList<>();

        try {
            File[] flist = beansDirectory.listFiles();
            if (flist.length == 0) {
                throw new MyFileException("Worker.searchBeans\n" + "Le répertoire que vous avez séléctionné ne contient pas de beans !", false);
            }
            for (File file : flist) {
                beansList.add(file);
            }
        } catch (MyFileException ex) {
            throw new MyFileException("Worker.searchBeans\n" + "Le répertoire que vous avez séléctionné ne contient pas de beans !", false);
        }
        return beansList;
    }

    public ArrayList<String> searchModels() {
        File modelsDirectory = new File("." + DEFAULT_FXML_PATH);
        File[] tableModels = modelsDirectory.listFiles();
        ArrayList<String> models = new ArrayList<>();

        for (File model : tableModels) {
            models.add(model.getName());
        }
        return models;
    }

    public void writeFile(Path path, byte[] bytes) throws MyFileException {
        try {
            Files.write(path, bytes);
        } catch (IOException ex) {
            throw new MyFileException("Worker.writeFxml\n" + "Erreur dans la génération de votre vue !", false);
        }
    }
}
