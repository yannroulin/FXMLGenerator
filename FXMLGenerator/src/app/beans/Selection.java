/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.beans;

/**
 *
 * @author RoulinY01
 */
public class Selection {

    private final String bean;
    private final String path;

    public Selection(String bean, String path) {
        this.bean = bean;
        this.path = path;
    }

    public String getBean() {
        return bean;
    }

    public String getPath() {
        return path;
    }

}
