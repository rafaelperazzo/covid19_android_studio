package pet.yoko.apps.covid;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MapaCidadesActivity extends AppCompatActivity {

    ProgressBar progresso;
    TextView aguarde;
    Button btnTabela;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_cidades);
        progresso = (ProgressBar)findViewById(R.id.progressoCidades);
        progresso.setVisibility(View.VISIBLE);
        aguarde = (TextView)findViewById(R.id.txtCidadesAguarde);
        btnTabela = (Button)findViewById(R.id.btnTabela);
        btnTabela.setVisibility(View.GONE);
        WebView webView = (WebView) findViewById(R.id.mapa_cidades);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                progresso.setVisibility(View.GONE);
                aguarde.setVisibility(View.GONE);
                btnTabela.setVisibility(View.VISIBLE);
            }
        });
        webView.loadUrl("https://apps.yoko.pet/covid/?q=1");
        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public void tabelaClick(View v) {
        Intent intent = new Intent(this, TabelaActivity.class);
        startActivity(intent);
    }
}
