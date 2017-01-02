package com.infostructure.mybigbro.ui.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.infostructure.mybigbro.R;
import com.infostructure.mybigbro.services.DataAccessService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class AboutActivity extends ActionBarActivity {

    /* UI controls */
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        /* Create the web view */
        this.mWebView = (WebView)findViewById(R.id.webView);
        this.mWebView.setInitialScale(1);
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.getSettings().setLoadWithOverviewMode(true);
        this.mWebView.getSettings().setUseWideViewPort(true);
        this.mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        this.mWebView.setScrollbarFadingEnabled(true);

        /* Load the site */
        new LoadData().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class  LoadData extends AsyncTask<Void, Void, Void> {

        String primeDiv = "divAbout";
        String html = new String();
        Document doc = null;

        @Override
        protected Void doInBackground(Void... params) {

            try {
                String url = "http://www.mybigbro.tv";
                doc = Jsoup.connect(url).timeout(100000).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //get total document
            Elements allDivs = doc.select("div");
            ArrayList<String> list = new ArrayList<String>();
            for (org.jsoup.nodes.Element e : allDivs) {
                if (!e.id().equals(""))
                    list.add(e.id());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mWebView.loadDataWithBaseURL(null, "<h1>" + doc.getElementById("divAbout").html() + "</h1>", "text/html", "utf-8", null);
        }
    }
}
