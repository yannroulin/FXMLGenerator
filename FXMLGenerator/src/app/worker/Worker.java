package app.worker;

import app.beans.Selection;
import app.exceptions.MyFileException;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implémentation de la couche "métier" de l'application.
 *
 * @author pa
 */
public class Worker implements WorkerItf {

    public static final String DEFAULT_MODELS_PATH = "\\src\\app\\models";

    @Override
    public ArrayList<String> searchBeans(File beansDirectory) throws MyFileException {
        ArrayList<String> filesNames = new ArrayList<>();
        ArrayList<Selection> beansList = new ArrayList<>();

        try {
            File[] flist = beansDirectory.listFiles();
            if (flist.length == 0) {
                throw new MyFileException("Worker.searchBeans\n" + "Le répertoire que vous avez séléctionné ne contient pas de beans !", false);
            }
            for (File file : flist) {
                filesNames.add(file.getName());
            }
        } catch (MyFileException ex) {
            throw new MyFileException("Worker.searchBeans\n" + "Le répertoire que vous avez séléctionné ne contient pas de beans !", false);
        }
        return filesNames;
    }

    @Override
    public ArrayList<String> searchModels() {
        File modelsDirectory = new File("." + DEFAULT_MODELS_PATH);
        File[] tableModels = modelsDirectory.listFiles();
        ArrayList<String> models = new ArrayList<>();

        for (File model : tableModels) {
            models.add(model.getName());
        }
        return models;
    }

    @Override
    public ArrayList<Selection> createSelection(File beansDirectory) throws MyFileException {

        ArrayList<Selection> beansList = new ArrayList<>();
        ArrayList<String> beanName;

        try {
            beanName = searchBeans(beansDirectory);
            for (String bean : beanName) {
                Selection s = new Selection(bean);
                beansList.add(s);
            }
        } catch (MyFileException ex) {
            throw new MyFileException("Worker.createSelection\n" + "Le répertoire que vous avez séléctionné ne contient pas de beans !", false);
        }
        return beansList;
    }

}
