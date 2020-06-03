package pet.yoko.apps.covid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MapaBairrosActivity extends AppCompatActivity {

    ProgressBar progresso;
    TextView aguarde;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_bairros);
        progresso = (ProgressBar)findViewById(R.id.progressoBairros);
        progresso.setVisibility(View.VISIBLE);
        aguarde = (TextView)findViewById(R.id.txtBairrosAguarde);
        WebView webView = (WebView) findViewById(R.id.mapa_bairros);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                progresso.setVisibility(View.GONE);
                aguarde.setVisibility(View.GONE);
            }
        });
        webView.loadUrl("https://apps.yoko.pet/covid/?q=2");
    }
}
