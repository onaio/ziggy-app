package org.ei.ziggy.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.webkit.WebSettings;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.ei.ziggy.ZiggyContext;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.MessageFormat;

import static java.util.UUID.randomUUID;
import static org.ei.ziggy.AllConstants.*;
import static org.ei.ziggy.R.string.*;
import static org.ei.ziggy.util.Log.logError;

public class FormActivity extends WebActivity {
    public static final String ANDROID_CONTEXT_FIELD = "androidContext";
    private String model;
    private String form;
    private String formName;
    private String entityId;
    private String fieldOverrides;

    @Override
    protected void onCreation() {
        try {
            getIntentData();
        } catch (IOException e) {
            logError(e.toString());
            finish();
        }
        webViewInitialization();
    }

    private void getIntentData() throws IOException {
        Intent intent = getIntent();
        formName = intent.getStringExtra(FORM_NAME_PARAM);
        entityId = intent.getStringExtra(ENTITY_ID_PARAM);
        fieldOverrides = intent.getStringExtra(FIELD_OVERRIDES_PARAM);
        model = IOUtils.toString(getAssets().open("www/form/" + formName + "/model.xml"));
        form = IOUtils.toString(getAssets().open("www/form/" + formName + "/form.xml"));
    }

    private void webViewInitialization() {
        WebSettings webViewSettings = webView.getSettings();
        webViewSettings.setJavaScriptEnabled(true);
        webViewSettings.setDatabaseEnabled(true);
        webViewSettings.setDomStorageEnabled(true);
        webView.addJavascriptInterface(new FormWebInterface(model, form), ANDROID_CONTEXT_FIELD);
        webView.addJavascriptInterface(ZiggyContext.getInstance().formDataRepository(), REPOSITORY);
        webView.addJavascriptInterface(ZiggyContext.getInstance().ziggyFileLoader(), ZIGGY_FILE_LOADER);
        webView.addJavascriptInterface(ZiggyContext.getInstance().formSubmissionRouter(), FORM_SUBMISSION_ROUTER);
        String encodedFieldOverrides = null;
        try {
            if (StringUtils.isNotBlank(this.fieldOverrides)) {
                encodedFieldOverrides = URLEncoder.encode(this.fieldOverrides, "utf-8");
            }
        } catch (Exception e) {
            logError(MessageFormat.format("Cannot encode field overrides: {0} due to : {1}", fieldOverrides, e));
        }
        webView.loadUrl(MessageFormat.format("file:///android_asset/www/enketo/template.html?{0}={1}&{2}={3}&{4}={5}&{6}={7}&touch=true",
                FORM_NAME_PARAM, formName,
                ENTITY_ID_PARAM, entityId,
                INSTANCE_ID_PARAM, randomUUID(),
                FIELD_OVERRIDES_PARAM, encodedFieldOverrides));
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage(form_back_confirm_dialog_message)
                .setTitle(form_back_confirm_dialog_title)
                .setCancelable(false)
                .setPositiveButton(yes_button_label,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                goBack();
                            }
                        })
                .setNegativeButton(no_button_label,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                            }
                        })
                .show();
    }

    private void goBack() {
        super.onBackPressed();
    }
}
