package pet.yoko.apps.covid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WebViewActivity extends AppCompatActivity {

    ProgressBar progresso;
    TextView aguarde;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        progresso = (ProgressBar)findViewById(R.id.progressoWebView);
        progresso.setVisibility(View.VISIBLE);
        aguarde = (TextView)findViewById(R.id.txtWebViewAguarde);
        WebView webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                progresso.setVisibility(View.GONE);
                aguarde.setVisibility(View.GONE);
            }
        });
        Intent intent = getIntent();
        String site = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        webView.loadUrl(site);
    }
}
