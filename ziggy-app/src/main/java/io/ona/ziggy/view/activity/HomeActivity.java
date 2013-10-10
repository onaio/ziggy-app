package io.ona.ziggy.view.activity;

public class HomeActivity extends WebFormActivity {
    @Override
    protected void onInitialization() {
        webView.loadUrl("file:///android_asset/clts/index.html");
    }
}
