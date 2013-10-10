package org.ei.ziggy.view.activity;

import android.content.Intent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.ei.ziggy.AllConstants;
import org.ei.ziggy.view.controller.FormController;

import java.util.Map;

import static org.ei.ziggy.AllConstants.ENTITY_ID_PARAM;
import static org.ei.ziggy.AllConstants.FIELD_OVERRIDES_PARAM;
import static org.ei.ziggy.AllConstants.FORM_NAME_PARAM;

public abstract class WebFormActivity extends WebActivity {
    @Override
    protected void onCreation() {
        webView.addJavascriptInterface(new FormController(this), "formContext");

        onInitialization();
    }

    public void startFormActivity(String formName, String entityId, String metadata) {
        Intent intent = new Intent(this, FormActivity.class);
        intent.putExtra(FORM_NAME_PARAM, formName);
        intent.putExtra(ENTITY_ID_PARAM, entityId);
        addFieldOverridesIfExist(intent, metadata);
        startActivity(intent);
    }

    private void addFieldOverridesIfExist(Intent intent, String metadata) {
        Map<String, String> metadataMap = new Gson().fromJson(metadata, new TypeToken<Map<String, String>>() {
        }.getType());
        if (metadataMap.containsKey(FIELD_OVERRIDES_PARAM)) {
            intent.putExtra(FIELD_OVERRIDES_PARAM, metadataMap.get(FIELD_OVERRIDES_PARAM));
        }
    }

    protected abstract void onInitialization();
}
