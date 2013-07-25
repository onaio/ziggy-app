package org.ei.ziggy.view.activity;

import static org.ei.ziggy.util.Log.logInfo;

public class FormWebInterface {
    private final String model;
    private final String form;

    public FormWebInterface(String model, String form) {
        this.model = model;
        this.form = form;
    }

    public String getModel() {
        return model;
    }

    public String getForm() {
        return form;
    }

    public void log(String message) {
        logInfo(message);
    }
}
