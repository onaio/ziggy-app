package org.ei.ziggy.view.activity;

import org.ei.ziggy.view.controller.VillageListController;

public class VillageListActivity extends WebFormActivity {
    @Override
    protected void onInitialization() {
        webView.addJavascriptInterface(new VillageListController(context.allVillages()), "context");
        webView.loadUrl("file:///android_asset/www/village_list.html");
    }
}
