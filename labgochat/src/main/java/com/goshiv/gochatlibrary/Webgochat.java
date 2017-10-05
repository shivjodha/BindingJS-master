package com.goshiv.gochatlibrary;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.util.Pair;
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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


        String GMID;
        String name;
        String email;
        String code;
        String phone;
        String type;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                GMID = null;
                name = null;
                email = null;
                code = null;
                phone = null;
                type = null;

            } else {
                GMID= extras.getString("GMID");
                name =extras.getString("name");
                email =extras.getString("email");
                code = extras.getString("code");
                phone = extras.getString("phone");
                type =extras.getString("type");
            }
        } else {
            GMID= (String) savedInstanceState.getSerializable("GMID");
            name = (String) savedInstanceState.getSerializable("name");
            email = (String) savedInstanceState.getSerializable("email");
            code = (String) savedInstanceState.getSerializable("code");
            phone = (String)savedInstanceState.getSerializable("phone");
            type = (String) savedInstanceState.getSerializable("type");
        }


        webView = (WebView) findViewById(R.id.webView);
       // embed_link = "http://droidmentor-app-callback.surge.sh/";

         embed_link = Uri.parse("http://192.168.1.113/projects/faisal/test/widget.php")
                .buildUpon()
                .appendQueryParameter("GMID", GMID)
                .appendQueryParameter("name",name)
                .appendQueryParameter("email",email)
                .appendQueryParameter("code",code)
                .appendQueryParameter("phone",phone)
                .appendQueryParameter("type",type)
                .build().toString();



      //  embed_link = "http://192.168.1.113/projects/faisal/test/chat.html";
        if (TextUtils.isEmpty(embed_link))
            onBackPressed();

        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
     // String value = addLocationToUrl(embed_link);
      Log.d("url",embed_link);
        Webviewmethod();

    }



    protected String addLocationToUrl(String url){
        if(!url.endsWith("?"))
            url += "?";

     /*   List<NameValuePair> params = new LinkedList<NameValuePair>();

        if (lat != 0.0 && lon != 0.0){
            params.add(new BasicNameValuePair("lat", String.valueOf(lat)));
            params.add(new BasicNameValuePair("lon", String.valueOf(lon)));
        }

        if (address != null && address.getPostalCode() != null)
            params.add(new BasicNameValuePair("postalCode", address.getPostalCode()));
        if (address != null && address.getCountryCode() != null)
            params.add(new BasicNameValuePair("country",address.getCountryCode()));

        params.add(new BasicNameValuePair("user", agent.uniqueId));

        String paramString = URLEncodedUtils.format(params, "utf-8");*/

        List<Pair<String, String>> params = new ArrayList<>();
        params.add(new Pair<>("GMID", "YWU2YzYwMGU4"));
        params.add(new Pair<>("name", "Faisal Ali"));
        params.add(new Pair<>("email", "faisalazmee1@gmail.com"));
        params.add(new Pair<>("code", "91"));
        params.add(new Pair<>("phone", "9833173457"));
        params.add(new Pair<>("type", "9833173457"));

       String  paramString =params.toString();
        url += paramString;
        return url;
    }


    public void Webviewmethod(){

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
