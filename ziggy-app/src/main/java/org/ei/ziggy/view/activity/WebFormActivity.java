package org.ei.ziggy.view.activity;

import android.content.Intent;
import org.ei.ziggy.view.controller.FormController;

import static org.ei.ziggy.AllConstants.ENTITY_ID_PARAM;
import static org.ei.ziggy.AllConstants.FORM_NAME_PARAM;

public abstract class WebFormActivity extends WebActivity {
    @Override
    protected void onCreation() {
        webView.addJavascriptInterface(new FormController(this), "formContext");

        onInitialization();
    }

    public void startFormActivity(String formName, String entityId) {
        Intent intent = new Intent(this, FormActivity.class);
        intent.putExtra(FORM_NAME_PARAM, formName);
        intent.putExtra(ENTITY_ID_PARAM, entityId);
        startActivity(intent);
    }

    protected abstract void onInitialization();
}
