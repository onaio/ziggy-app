package org.ei.ziggy.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.*;
import org.ei.ziggy.ZiggyContext;
import org.ei.ziggy.R;
import org.ei.ziggy.domain.FetchStatus;
import org.ei.ziggy.sync.AfterFetchListener;
import org.ei.ziggy.sync.ProgressIndicator;
import org.ei.ziggy.sync.UpdateTask;
import org.ei.ziggy.view.controller.NavigationController;

import static android.webkit.ConsoleMessage.MessageLevel.ERROR;
import static java.text.MessageFormat.format;
import static org.ei.ziggy.domain.FetchStatus.fetched;
import static org.ei.ziggy.util.Log.logDebug;
import static org.ei.ziggy.util.Log.logError;

public abstract class WebActivity extends Activity {
    protected WebView webView;
    protected ZiggyContext context;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = ZiggyContext.getInstance().updateApplicationContext(this.getApplicationContext());
        context.userService().initUser("c", "1"); //Hard coded for Dristhi
        setContentView(R.layout.web);

        progressDialogInitialization();
        webViewInitialization(this);

        onCreation();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.updateMenuItem:
                updateFromServer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    private void updateFromServer() {
        UpdateTask updateTask = new UpdateTask(getApplicationContext(), context.formSubmissionSyncService(), new ProgressIndicator() {
            @Override
            public void setVisible() {
            }

            @Override
            public void setInvisible() {
            }
        });

        updateTask.updateFromServer(new AfterFetchListener() {
            public void afterFetch(FetchStatus status) {
                if (fetched.equals(status)) {
                }
            }
        });
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

            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });

        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.addJavascriptInterface(new NavigationController(this), "navigationContext");
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
    }

    protected abstract void onCreation();
}
