package org.ei.ziggy.view.activity;

public class HomeActivity extends WebActivity {
    @Override
    protected void onCreation() {
        webView.loadUrl("file:///android_asset/www/home.html");
    }
}
