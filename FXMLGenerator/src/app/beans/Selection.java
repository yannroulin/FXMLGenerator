package app.beans;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Selection {

    private final StringProperty bean = new SimpleStringProperty();
    private final StringProperty model = new SimpleStringProperty();
    private final StringProperty path = new SimpleStringProperty();

    /**
     * Constructeur de la classe Bean Selection. Initialise les attributs.
     *
     * @param bean String contenant le nom du bean
     * @param path String contenant le modèle du bean
     * @param model String contenant le chemin de fichier du bean
     */
    public Selection(String bean, String path, String model) {
        setBean(bean);
        setModel(model);
        setPath(path);
    }
    
    public final StringProperty beanProperty() {
        return this.bean;
    }

    public final String getBean() {
        return this.beanProperty().get();
    }

    public final void setBean(final String bean) {
        this.beanProperty().set(bean);
    }

    public final StringProperty pathProperty() {
        return this.path;
    }

    public final String getPath() {
        return this.pathProperty().get();
    }

    public final void setPath(final String path) {
        this.pathProperty().set(path);
    }

    public final StringProperty modelProperty() {
        return this.model;
    }

    public final String getModel() {
        return this.modelProperty().get();
    }

    public final void setModel(final String model) {
        this.modelProperty().set(model);
    }
}
