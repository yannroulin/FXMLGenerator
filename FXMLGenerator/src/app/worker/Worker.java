package app.worker;

import app.beans.Selection;
import app.exceptions.MyFileException;
import java.io.File;
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

    private final WorkerFile wrk;
    public static final String PATH_TO_MODEL = ".\\src\\app\\models\\FormView.fxml";
    public static final String PATH_TO_CTRL = ".\\src\\app\\utils\\CtrlFormModel.java";
    public static final String PATH_TO_MAINVIEW = ".\\src\\app\\utils\\MainView.fxml";
    public static final String PATH_TO_MAINCTRL = ".\\src\\app\\utils\\MainCtrl.java";

    public Worker() {
        wrk = new WorkerFile();
    }

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
        } catch (MyFileException ex) {
            throw new MyFileException("Worker.createSelection\n" + "Le répertoire que vous avez séléctionné ne contient pas de beans !", false);
        }
        return beansList;
    }

    @Override
    public void getAttributesofBeans(ObservableList<Selection> selected) throws MyFileException {

        String filePath;

        for (Selection beanInfo : selected) {

            filePath = beanInfo.getPath();
            byte[] tab;

            ArrayList<String> attributes = new ArrayList<>();
            List<String> lines = wrk.readFiles(filePath);

            for (String line : lines) {
                if (line.contains("private")) {
                    attributes.add(line);
                }
            }
            prepareFxml(attributes, beanInfo, PATH_TO_MODEL);
            prepareCtrl(attributes, beanInfo, PATH_TO_CTRL);
            prepareMainView(selected, PATH_TO_MAINVIEW);
            prepareMainCtrl(PATH_TO_MAINCTRL, beanInfo);
        }
    }

    @Override
    public void prepareFxml(ArrayList<String> list, Selection bean, String pathToModel) throws MyFileException {
        String xmlFileForm = "";
        String xmlFileList = "";
        String[] tab;
        int rowIndex = -1;
        int columnIndex = -1;
        byte[] bytes = null;

        List<String> linesOfFxmlFile = wrk.readFiles(pathToModel);

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
        wrk.writeFile(path, bytes);
    }

    @Override
    public void prepareCtrl(ArrayList<String> list, Selection bean, String pathToCtrl) throws MyFileException {
        String[] tab;
        byte[] bytes = null;
        List<String> linesOfCtrlFile = wrk.readFiles(pathToCtrl);
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
        wrk.writeFile(path, bytes);
    }

    @Override
    public void prepareMainView(ObservableList<Selection> listBeans, String path) throws MyFileException {

        List<String> mainFxmlContent = wrk.readFiles(path);
        String linesToAdd = "";
        String content = "";
        String destinationFolder = "";
        byte[] bytes = null;
        String[] tab;

        for (Selection beanSelect : listBeans) {
            String fxmlFileName = beanSelect.getBean().replace(".java", "View.fxml");
            String txt = beanSelect.getBean().replace(".java", "");
            linesToAdd += "<Tab fx:id=\"tab" + beanSelect.getBean() + "\" text=\"" + txt + "\">\n<content>\n<fx:include fx:id=\"ID" + beanSelect.getBean() + "\" source=\"" + fxmlFileName + "\" /> \n </content>\n</Tab>";

            destinationFolder = beanSelect.getPath().replace(beanSelect.getBean(), "");
        }

        destinationFolder += "..\\models\\MainView.fxml";
        Path pathFinalDirectory = Paths.get(destinationFolder);

        for (String line : mainFxmlContent) {
            if (line.contains("<!--insert here-->")) {
                content += "\n" + linesToAdd;
            } else {
                content += line;
            }
            bytes = content.getBytes();
        }
        wrk.writeFile(pathFinalDirectory, bytes);
    }

    @Override
    public void prepareMainCtrl(String pathofCtrl, Selection bean) throws MyFileException {
        List<String> mainCtrlContent = wrk.readFiles(pathofCtrl);
        String content = "";
        byte[] bytes = null;

        for (String line : mainCtrlContent) {
            content += line;
        }

        String destinationFolder = bean.getPath().replace(bean.getBean(), "");
        destinationFolder += "..\\models\\MainCtrl.java";

        Path pathFinalDirectory = Paths.get(destinationFolder);

        bytes = content.getBytes();

        wrk.writeFile(pathFinalDirectory, bytes);
    }

    @Override
    public ArrayList<File> searchBeans(File beansDirectory) throws MyFileException {
        return wrk.searchBeans(beansDirectory);
    }

    @Override
    public ArrayList<String> searchModels() {
        return wrk.searchModels();
    }
}
