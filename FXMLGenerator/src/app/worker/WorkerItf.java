package app.worker;

import app.beans.Selection;
import app.exceptions.MyFileException;
import java.io.File;
import java.util.ArrayList;

/**
 * Cette interface définit les services "métier" de l'application.
 *
 * @author ...
 */
public interface WorkerItf {

    ArrayList<String> searchBeans(File beansDirectory) throws MyFileException;

    ArrayList<String> searchModels();

    ArrayList<Selection> createSelection(File beansDirectory) throws MyFileException;

}
