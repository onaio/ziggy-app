package org.ei.ziggy.view.activity;

public class VillageListActivity extends WebFormActivity {
    @Override
    protected void onInitialization() {
        webView.loadUrl("file:///android_asset/www/village_list.html");
    }
}
