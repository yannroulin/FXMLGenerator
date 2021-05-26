package app.worker;

import app.beans.Selection;
import app.helpers.JfxPopup;
import java.io.File;
import java.util.ArrayList;

/**
 * Implémentation de la couche "métier" de l'application.
 *
 * @author pa
 */
public class Worker implements WorkerItf {

    public static final String DEFAULT_MODELS_PATH = "\\src\\app\\models";

    @Override
    public ArrayList<String> searchBeans(File beansDirectory) {
        ArrayList<String> filesNames = new ArrayList<>();
        try {
            File[] flist = beansDirectory.listFiles();
            for (File file : flist) {
                filesNames.add(file.getName());
            }
        } catch (Exception ex) {
            //
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
    public ArrayList<Selection> createSelection(File beansDirectory) {

        ArrayList<Selection> beansList = new ArrayList<>();
        ArrayList<String> beanName = searchBeans(beansDirectory);

        for (String bean : beanName) {
            Selection s = new Selection(bean);
            beansList.add(s);
        }
        return beansList;
    }

}
