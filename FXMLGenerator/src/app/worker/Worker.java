package app.worker;

import app.beans.Selection;
import app.exceptions.MyFileException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;

/**
 * Implémentation de la couche "métier" de l'application.
 *
 * @author pa
 */
public class Worker implements WorkerItf {

    public static final String DEFAULT_FXML_PATH = "\\src\\app\\viewsmodels\\";
    public static final String DEFAULT_CTRL_PATH = "\\src\\app\\ctrlmodels\\";

    @Override
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

    @Override
    public ArrayList<String> searchModels() {
        File modelsDirectory = new File("." + DEFAULT_FXML_PATH);
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
        ArrayList<File> beanName;

        try {
            beanName = searchBeans(beansDirectory);
            for (File bean : beanName) {
                Selection s = new Selection(bean.getName(), bean.getPath());
                beansList.add(s);
            }
        } catch (MyFileException ex) {
            throw new MyFileException("Worker.createSelection\n" + "Le répertoire que vous avez séléctionné ne contient pas de beans !", false);
        }
        return beansList;
    }

    @Override
    public void readBeans(ObservableList<Selection> selected) throws MyFileException {

        String filePath;

        for (Selection beanInfo : selected) {
            if (beanInfo.getPath() == null) {
                throw new MyFileException("Worker.lireFichiers\n" + "Chemin de fichier null", false);
            }
            filePath = beanInfo.getPath();

            Path file = Paths.get(filePath);
            byte[] tab;

            try {
                ArrayList<String> attributes = new ArrayList<>();
                tab = Files.readAllBytes(file);
                List<String> lines = Files.readAllLines(file, Charset.forName("UTF-8"));

                for (String line : lines) {
                    if (line.contains("private")) {
                        attributes.add(line);
                    }
                }
                writeFxml(attributes, beanInfo);
                writeCtrl(attributes, beanInfo);
            } catch (IOException e) {
                throw new MyFileException("Worker.readBeans\n" + "Lecture de fichier impossible", false);
            }

        }

    }

    @Override
    public void writeFxml(ArrayList<String> list, Selection bean) throws MyFileException {
        String xmlFileForm = "";
        String xmlFileList = "";
        String[] tab;
        int rowIndex = -1;
        int columnIndex = -1;

        byte[] bytes = null;
        List<String> linesOfFxmlFile = readFxml();

        for (String attributes : list) {
            rowIndex++;
            columnIndex++;
            tab = attributes.split("\\s+");
            String replaceAttribut = tab[3].replace(";", "");

            switch (tab[2]) {
                case "String":
                    xmlFileForm += "<Label fx:id=\"" + "lbl" + replaceAttribut + "\" prefHeight=\"17.0\" prefWidth=\"122.0\" text=\"" + replaceAttribut + "\" GridPane.rowIndex=\"" + rowIndex + "\"> "
                            + "<GridPane.margin>\n <Insets bottom=\"20.0\" left=\"10.0\" right=\"10.0\" top=\"20.0\" />\n</GridPane.margin> </Label>\n"
                            + "<TextField fx:id=\"" + "txt" + replaceAttribut + "\" prefHeight=\"25.0\" prefWidth=\"193.0\" GridPane.columnIndex=\"1\" GridPane.rowIndex=\"" + rowIndex + "\" > \n"
                            + "<GridPane.margin>\n <Insets left=\"20.0\" right=\"20.0\"/>\n </GridPane.margin>\n </TextField>";
                    break;
                case "int":
                    xmlFileForm += "<Label fx:id=\"" + "lbl" + replaceAttribut + "\" prefHeight=\"17.0\" prefWidth=\"122.0\" text=\"" + replaceAttribut + "\" GridPane.rowIndex=\"" + rowIndex + "\"> "
                            + "<GridPane.margin>\n <Insets bottom=\"20.0\" left=\"10.0\" right=\"10.0\" top=\"20.0\" />\n</GridPane.margin> </Label>\n"
                            + "<TextField fx:id=\"" + "txt" + replaceAttribut + "\" prefHeight=\"25.0\" prefWidth=\"193.0\" GridPane.columnIndex=\"1\" GridPane.rowIndex=\"" + rowIndex + "\" > \n"
                            + "<GridPane.margin>\n <Insets left=\"20.0\" right=\"20.0\"/>\n </GridPane.margin>\n </TextField>";
                    break;
                default:
                // code block
                }
        }

        String destinationFolder = bean.getPath().replace(bean.getBean(), "");
        destinationFolder += "..\\models\\";
        String link = bean.getBean().replace(".java", "Ctrl");
        String fileName = bean.getBean().replace(".java", "View.fxml");
        Path path = Paths.get(destinationFolder + fileName);
        String content = "";

        for (String line : linesOfFxmlFile) {
            if (line.contains("<!--insert here-->")) {
                content += "\n" + xmlFileForm;
            } else if (line.contains("fx:controller=\"\"")) {
                content += "<BorderPane maxHeight=\"-Infinity\" maxWidth=\"-Infinity\" minHeight=\"-Infinity\" minWidth=\"-Infinity\" prefHeight=\"524.0\" prefWidth=\"600.0\" xmlns=\"http://javafx.com/javafx/11.0.1\" xmlns:fx=\"http://javafx.com/fxml/1\" fx:controller=\"app.models." + link + "\">\n";
            } else {
                content += line;
            }
            bytes = content.getBytes();
        }

        try {
            Files.write(path, bytes);
        } catch (IOException ex) {
            throw new MyFileException("Worker.writeFxml\n" + "Erreur dans la génération de votre vue !", false);
        }
    }

    @Override
    public void writeCtrl(ArrayList<String> list, Selection bean) throws MyFileException {
        String[] tab;
        byte[] bytes = null;
        List<String> linesOfCtrlFile = readCtrl();
        String linesToAdd = "";

        for (String attributes : list) {
            linesToAdd += "@FXML\n" + attributes;
        }

        String destinationFolder = bean.getPath().replace(bean.getBean(), "");
        destinationFolder += "..\\models\\";
        String fileName = bean.getBean().replace(".java", "Ctrl.java");
        Path path = Paths.get(destinationFolder + fileName);
        String className = fileName.replace(".java", "");
        String content = "";

        for (String line : linesOfCtrlFile) {
            if (line.contains("//insert here")) {
                content += "\n" + linesToAdd;
            } else if (line.contains("public class CtrlFormModel implements Initializable {")) {
                content += "public class " + className + " implements Initializable {";
            } else {
                content += line;
            }
            bytes = content.getBytes();
        }

        try {
            Files.write(path, bytes);
        } catch (IOException ex) {
            throw new MyFileException("Worker.writeCtrl\n" + "Erreur dans la génération du contrôleur de votre vue !", false);
        }

    }

    @Override
    public List<String> readFxml() throws MyFileException {
        byte[] bytesTab;
        List<String> lines;
        Path finalPath;

        finalPath = Paths.get("." + DEFAULT_FXML_PATH + "FormView.fxml");

        try {
            bytesTab = Files.readAllBytes(finalPath);
            lines = Files.readAllLines(finalPath, Charset.forName("UTF-8"));

        } catch (IOException ex) {
            throw new MyFileException("Worker.readFxml\n" + "Lecture de fichier impossible", false);
        }
        return lines;
    }

    @Override
    public List<String> readCtrl() throws MyFileException {
        byte[] bytesTab;
        List<String> lines;
        Path finalPath;

        finalPath = Paths.get("." + DEFAULT_CTRL_PATH + "CtrlFormModel.java");

        try {
            bytesTab = Files.readAllBytes(finalPath);
            lines = Files.readAllLines(finalPath, Charset.forName("UTF-8"));
        } catch (IOException ex) {
            throw new MyFileException("Worker.readCtrl\n" + "Lecture de fichier impossible", false);
        }
        return lines;
    }

}
