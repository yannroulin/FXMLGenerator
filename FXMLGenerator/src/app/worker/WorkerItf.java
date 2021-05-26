package app.worker;

import app.beans.Selection;
import java.io.File;
import java.util.ArrayList;

/**
 * Cette interface définit les services "métier" de l'application.
 *
 * @author ...
 */
public interface WorkerItf {

    ArrayList<String> searchBeans(File beansDirectory);
    ArrayList<String> searchModels();
    ArrayList<Selection> createSelection(File beansDirectory);

}
