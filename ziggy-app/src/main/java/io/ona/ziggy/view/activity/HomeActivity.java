package org.ei.ziggy.view.activity;

public class HomeActivity extends WebFormActivity {
    @Override
    protected void onInitialization() {
        webView.loadUrl("file:///android_asset/www/home.html");
    }
}
