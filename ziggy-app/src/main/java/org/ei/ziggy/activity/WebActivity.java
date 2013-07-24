package org.ei.ziggy.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import org.ei.ziggy.ZiggyContext;
import org.ei.ziggy.R;

import static android.webkit.ConsoleMessage.MessageLevel.ERROR;
import static java.text.MessageFormat.format;
import static org.ei.ziggy.util.Log.logDebug;
import static org.ei.ziggy.util.Log.logError;

public abstract class WebActivity extends Activity {
    protected WebView webView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ZiggyContext.getInstance().updateApplicationContext(this.getApplicationContext());

        setContentView(R.layout.web);

        progressDialogInitialization();
        webViewInitialization(this);

        onCreation();
    }

    private void progressDialogInitialization() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading ...");
        progressDialog.setMessage("Please wait");
        progressDialog.show();
    }

    private void webViewInitialization(final Activity activity) {
        webView = (WebView) findViewById(R.id.webview);

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                activity.setProgress(progress * 1000);

                if (progress == 100 && progressDialog.isShowing())
                    progressDialog.dismiss();
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                String message = format("Javascript Log. Message: {0}, lineNumber: {1}, sourceId, {2}", consoleMessage.message(),
                        consoleMessage.lineNumber(), consoleMessage.sourceId());

                if (consoleMessage.messageLevel() == ERROR) {
                    logError(message);
                } else {
                    logDebug(message);
                }
                return true;
            }
        });

        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
    }

    protected abstract void onCreation();
}
