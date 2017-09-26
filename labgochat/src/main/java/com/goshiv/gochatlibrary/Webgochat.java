package com.goshiv.gochatlibrary;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class Webgochat extends AppCompatActivity {

    private ProgressUtils progressUtils;
    private WebView webView;
    private String embed_link;
    private JavaScriptReceiver javaScriptReceiver;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressUtils = new ProgressUtils(this);

        webView = (WebView) findViewById(R.id.webView);
       // embed_link = "http://droidmentor-app-callback.surge.sh/";
        embed_link = "http://192.168.1.113/projects/faisal/test/chat.html";
        if (TextUtils.isEmpty(embed_link))
            onBackPressed();

        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);


        if (NetworkCheck.isNetworkAvailable(this)) {

            progressUtils.show_dialog(true);
            javaScriptReceiver = new JavaScriptReceiver(this);
            webView.addJavascriptInterface(javaScriptReceiver, "JSReceiver");
            webView.loadUrl(embed_link);
        }

        webView.setWebViewClient(new WebViewClient() {

            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String requestUrl) {

                Log.i("loading", "" + requestUrl);
                view.loadUrl(requestUrl);
                return true;
            }

            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest req) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                shouldOverrideUrlLoading(view, req.getUrl().toString());
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap facIcon) {
                Log.i("started", "" + url);

            }

            public void onPageFinished(WebView view, String url) {
                // ////Log.i("TAG", "Finished loading URL: " +url);
                Log.i("finished", "" + url);

                if (progressUtils != null && url.equals(embed_link)) {
                    progressUtils.dismiss_dialog();
                    progressUtils = null;
                }
            }

            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e("TAG", "Error: " + description);
                //Log.i("error", "" + failingUrl);

                if (progressUtils != null) {
                    progressUtils.dismiss_dialog();
                    progressUtils = null;
                    Toast.makeText(Webgochat.this, " Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }
        });

    }
}
