package app.worker;

import app.beans.Selection;
import app.exceptions.MyFileException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;

/**
 * Cette interface définit les services "métier" de l'application.
 *
 * @author ...
 */
public interface WorkerItf {

    ArrayList<File> searchBeans(File beansDirectory) throws MyFileException;

    ArrayList<String> searchModels();

    ArrayList<Selection> createSelection(File beansDirectory) throws MyFileException;

    void readBeans(ObservableList<Selection> selected) throws MyFileException;
    
    void writeFxml(ArrayList<String> list, Selection bean) throws MyFileException;
    
    void writeCtrl(ArrayList<String> list, Selection bean) throws MyFileException;
    
    List<String> readFxml() throws MyFileException;
    
    List<String> readCtrl() throws MyFileException;
}
