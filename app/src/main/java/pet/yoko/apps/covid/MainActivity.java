package pet.yoko.apps.covid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import com.github.anastr.speedviewlib.SpeedView;
import com.github.anastr.speedviewlib.Speedometer;
import com.github.anastr.speedviewlib.TubeSpeedometer;
import com.github.anastr.speedviewlib.components.Section;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pet.yoko.apps.covid.db.CarregarCidades;
import pet.yoko.apps.covid.db.CarregarCoeficiente;
import pet.yoko.apps.covid.db.CarregarDadosIniciais;
import pet.yoko.apps.covid.db.DadosIniciais;
import pet.yoko.apps.covid.db.DatabaseClient;
import pet.yoko.apps.covid.db.DownloadData;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    public static final String TIPO = "confirmados";
    public static final String TITULO = "Confirmações por ";
    public static final String TIPO_GRAFICO = "bar";
    public static final String DESCRICAO_GRAFICO = "DESCRIÇÃO";
    public static final String TIPO_MAPA = "cidades";
    public static final String CIDADE = "TODAS AS CIDADES";
    public String texto_descricao_grafico = "";
    public String url = "https://apps.yoko.pet/webapi/covidapi.php?resumo=";
    public String url_cidades = "https://apps.yoko.pet/webapi/covidapi.php?dados=1&tipo=cidades";
    TextView confirmados;
    TextView suspeitos;
    TextView obitos;
    TextView atualizacao;
    TextView txtUti;
    TextView txtEnfermaria;
    ProgressBar progresso;
    TextView versao;
    TextView txtDias;
    TextView txtHoras;
    TextView txtMinutos;
    public int VERSAO;
    TextView atualizar;
    TextView txtAvisos;
    SharedPreferences sharedPref;
    SpeedView velocimetro;
    TubeSpeedometer velocimetro2;
    Spinner cidade;
    TextView txtSituacao;
    ImageView imgClassificacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgClassificacao = (ImageView)findViewById(R.id.imgClassificacao);
        imgClassificacao.setVisibility(View.GONE);
        atualizacao = (TextView)findViewById(R.id.atualizacao);
        confirmados = (TextView)findViewById(R.id.confirmados);
        suspeitos = (TextView)findViewById(R.id.suspeitos);
        obitos = (TextView)findViewById(R.id.obitos);
        txtUti = (TextView)findViewById(R.id.txtUTI);
        txtEnfermaria = (TextView)findViewById(R.id.txtEnfermaria);
        txtDias = (TextView)findViewById(R.id.txtDias);
        txtHoras = (TextView)findViewById(R.id.txtHoras);
        txtMinutos = (TextView)findViewById(R.id.txtMinutos);
        txtAvisos = (TextView)findViewById(R.id.txtAvisos);
        progresso = (ProgressBar)findViewById(R.id.progresso);
        versao = (TextView)findViewById(R.id.txtVersao);
        atualizar = (TextView)findViewById(R.id.txtAtualizar);
        atualizar.setVisibility(View.GONE);
        velocimetro = (SpeedView)findViewById(R.id.velocimetro);
        velocimetro2 = (TubeSpeedometer) findViewById(R.id.velocimetro2);
        cidade = (Spinner)findViewById(R.id.cmbCidades);
        txtSituacao = (TextView)findViewById(R.id.txtSituacao);
        this.onSelectCity();
        ajustarVelocimetro(velocimetro);
        ajustarVelocimetroTube(velocimetro2);
        String versionName;
        int versionCode;
        versao.setText("Versão: " + String.valueOf(getVersionCode()));
        VERSAO = getVersionCode();
        txtAvisos.setVisibility(View.GONE);
        sharedPref = getPreferences(Context.MODE_PRIVATE);

        //CARREGANDO DADOS INICIAIS
        if (!getAtualizacao().equals("00/00/0000 00:00")) {
            this.carregarDadosIniciais();
        }
        try {
            run("https://apps.yoko.pet/webapi/covidapi.php?dados=1&tipo=ultimaAtualizacao",0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //VERIFICANDO POR ATUALIZAÇÃO
        try {
            run("https://play.google.com/store/apps/details?id=pet.yoko.apps.covid",1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //RECEBENDO OS AVISOS
        try {
            this.run("https://apps.yoko.pet/webapi/covidapi.php?dados=1&tipo=avisos",5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setTitle("COVID19 APP - Totais");
    }

    @Override
    public void onResume(){
        super.onResume();


    }

    public void situacaoClick(View v) {
        if (imgClassificacao.getVisibility()==View.GONE) {
            imgClassificacao.setVisibility(View.VISIBLE);
        }
        else {
            imgClassificacao.setVisibility(View.GONE);
        }
    }

    public void classificacaoClick(View v) {
        imgClassificacao.setVisibility(View.GONE);
    }

    public void onSelectCity() {
        cidade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0) { //TODAS AS CIDADES
                    carregarDadosIniciais();
                }
                else {
                    carregarDadosIniciaisCidade(cidade.getSelectedItem().toString(),"cidadeUnica");
                    //carregarDadosCoeficiente(velocimetro2,1);
                    carregarDadosCoeficiente(velocimetro,0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void ajustarVelocimetro(SpeedView velocimetro) {
        velocimetro.setSpeedometerMode(Speedometer.Mode.NORMAL);
        velocimetro.setUnit("");
        velocimetro.setWithIndicatorLight(true);
        velocimetro.setSpeedTextSize(32);
        velocimetro.setMaxSpeed(200);
        velocimetro.clearSections();
        velocimetro.addSections(new Section(.0f,.06f,Color.GREEN,velocimetro.getSpeedometerWidth(),Section.Style.SQUARE));
        velocimetro.addSections(new Section(.06f,.17f,Color.YELLOW,velocimetro.getSpeedometerWidth(),Section.Style.SQUARE));
        velocimetro.addSections(new Section(.17f,.32f,Color.parseColor("#FF8000"),velocimetro.getSpeedometerWidth(),Section.Style.SQUARE));
        velocimetro.addSections(new Section(.32f,.55f,Color.RED,velocimetro.getSpeedometerWidth(),Section.Style.SQUARE));
        velocimetro.addSections(new Section(.55f,1f,Color.parseColor("#660066"),velocimetro.getSpeedometerWidth(),Section.Style.SQUARE));
        velocimetro.setWithTremble(false);
        //velocimetro.addNote(new TextNote(getApplicationContext(),"dd"), Note.INFINITE);
        velocimetro.setCenterCircleRadius(30);
    }

    public void ajustarVelocimetroTube(TubeSpeedometer velocimetro) {
        velocimetro.setSpeedometerMode(Speedometer.Mode.NORMAL);
        velocimetro.setUnit("%");
        velocimetro.setWithIndicatorLight(true);
        velocimetro.setSpeedTextSize(32);
        velocimetro.setMaxSpeed(100);
        velocimetro.clearSections();
        velocimetro.addSections(new Section(.0f,.7f,Color.GREEN,velocimetro.getSpeedometerWidth(),Section.Style.SQUARE));
        velocimetro.addSections(new Section(.7f,.85f,Color.YELLOW,velocimetro.getSpeedometerWidth(),Section.Style.SQUARE));
        velocimetro.addSections(new Section(.85f,.95f,Color.RED,velocimetro.getSpeedometerWidth(),Section.Style.SQUARE));
        velocimetro.addSections(new Section(.95f,1f,Color.parseColor("#660066"),velocimetro.getSpeedometerWidth(),Section.Style.SQUARE));
        velocimetro.setWithTremble(false);
        velocimetro.setUnitUnderSpeedText(true);
        //velocimetro.addNote(new TextNote(getApplicationContext(),"dd"), Note.INFINITE);
    }

    private void setAtualizacao(String atualizacao) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("atualizacao", atualizacao);
        editor.commit();
    }

    private String getAtualizacao() {
        String value = "00/00/0000 00:00";
        String atualizacao = sharedPref.getString("atualizacao",value);
        return (atualizacao);
    }

    public double coeficiente2Velocidade(double coeficiente) {
        double retorno;
        retorno = 100*(2-(1+(Math.log(coeficiente)/Math.log(100))));
        return (retorno);
    }

    public void carregarDadosIniciaisCidade(String cidade, String tipo) {
        CarregarCidades cc = new CarregarCidades(DatabaseClient.getInstance(getApplicationContext()).getAppDatabase(),tipo,cidade);
        try {
            ArrayList<CidadeItem> dados = (ArrayList<CidadeItem>)cc.execute().get();
            CidadeItem cidadeAtual = dados.get(0);
            this.confirmados.setText(String.valueOf(cidadeAtual.getConfirmados()));
            this.suspeitos.setText(String.valueOf(cidadeAtual.getEmRecuperacao()));
            this.obitos.setText(String.valueOf(cidadeAtual.getObitos()));

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public void carregarDadosIniciais() {
        CarregarDadosIniciais cdi = new CarregarDadosIniciais(DatabaseClient.getInstance(getApplicationContext()).getAppDatabase(),confirmados,suspeitos,obitos,txtUti,txtEnfermaria,atualizacao);
        List<DadosIniciais> items;
        try {
            items = cdi.execute().get();
            velocimetro2.speedTo(items.get(0).getUti());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //carregarDadosCoeficiente(velocimetro2,1);
        carregarDadosCoeficiente(velocimetro,0);
    }

    private void carregarDadosCoeficiente(SpeedView velocimetro, int tipo) {
        CarregarCoeficiente cc = new CarregarCoeficiente(DatabaseClient.getInstance(getApplicationContext()).getAppDatabase(),cidade.getSelectedItem().toString(),tipo);
        try {
            double coeficiente = cc.execute().get();
            double velocidade;
            int dias = (int) coeficiente;
            double resto = coeficiente - dias;
            int horas = (int)(resto*24);
            resto = (resto*24)-horas;
            int minutos = (int)(resto*60);
            if (coeficiente<1000) {
                txtDias.setText(String.valueOf(dias));
                txtHoras.setText(String.valueOf(horas));
                txtMinutos.setText(String.valueOf(minutos));
            }
            else {
                txtDias.setText(String.valueOf(0));
                txtHoras.setText(String.valueOf(0));
                txtMinutos.setText(String.valueOf(0));
            }
            if (coeficiente>100) {
                velocidade = 0;
            }
            else if (coeficiente<0.01) {
                velocidade = 200;
            }
            else {
                velocidade = coeficiente2Velocidade(coeficiente);
            }
            velocimetro.speedTo((float)velocidade,3000);
            DecimalFormat df = new DecimalFormat("#0.00");
            texto_descricao_grafico = df.format(coeficiente);
            if ((coeficiente>=0) && (coeficiente<0.58)) {
                this.txtSituacao.setText("GRAVE");
                this.txtSituacao.setBackgroundColor(Color.RED);
                this.txtSituacao.setTextColor(Color.WHITE);

            }
            else if ((coeficiente>=0.58) && (coeficiente<5)) {
                this.txtSituacao.setText("ALERTA");
                this.txtSituacao.setBackgroundColor(Color.parseColor("#FF3333"));

            }
            else if ((coeficiente>=5) && (coeficiente<20)) {
                this.txtSituacao.setText("ATENÇÃO");
                this.txtSituacao.setBackgroundColor(Color.parseColor("#FF8000"));

            }
            else if ((coeficiente>=20) && (coeficiente<56.8)) {
                this.txtSituacao.setText("CONTROLADA");
                this.txtSituacao.setBackgroundColor(Color.YELLOW);

            }
            else {
                this.txtSituacao.setText("NORMALIDADE");
                this.txtSituacao.setBackgroundColor(Color.parseColor("#336600"));
                this.txtSituacao.setTextColor(Color.WHITE);

            }
            velocimetro.removeAllNotes();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void atualizarClick(View v) {
        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=pet.yoko.apps.covid");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public int getVersionCode() {
        int versionCode = 0;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return (versionCode);
    }

    public void showMapasMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);

        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.mapas);
        popup.show();
    }

    public void showIdadeMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);

        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.menuidade);
        popup.show();
    }

    public void showSexoMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.menusexo);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.mapas_cidades:
                intent = new Intent(this, MapaActivity.class);
                intent.putExtra(TIPO_MAPA,"cidades");
                startActivity(intent);
                return true;
            case R.id.mapas_bairros:
                intent = new Intent(this, MapaActivity.class);
                intent.putExtra(TIPO_MAPA,"bairros");
                startActivity(intent);
                return true;
            case R.id.idade_confirmados:
                intent = new Intent(this, ChartActivity.class);
                intent.putExtra(TIPO,"idade");
                intent.putExtra(TIPO_GRAFICO,"bar");
                intent.putExtra(TITULO,"Casos confirmados por idade (totais)");
                startActivity(intent);
                return true;
            case R.id.idade_obitos:
                intent = new Intent(this, ChartActivity.class);
                intent.putExtra(TIPO,"obitosPorIdade");
                intent.putExtra(TIPO_GRAFICO,"bar");
                intent.putExtra(TITULO,"Óbitos confirmados por idade (totais)");
                startActivity(intent);
                return true;
            case R.id.sexo_confirmados:
                intent = new Intent(this, ChartActivity.class);
                intent.putExtra(TIPO,"sexo");
                intent.putExtra(TIPO_GRAFICO,"bar");
                intent.putExtra(TITULO,"Casos confirmados por sexo (totais)");
                startActivity(intent);
                return true;
            case R.id.sexo_obitos:
                intent = new Intent(this, ChartActivity.class);
                intent.putExtra(TIPO,"obitosPorSexo");
                intent.putExtra(TIPO_GRAFICO,"bar");
                intent.putExtra(TITULO,"Óbitos confirmados por sexo (totais)");
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        Intent intent;
        switch (item.getItemId()) {

            case R.id.menu_evolucao_temporal:
                intent =  new Intent(this,ChartActivity.class);
                intent.putExtra(TIPO,"evolucao");
                intent.putExtra(CIDADE,cidade.getSelectedItem().toString());
                intent.putExtra(TITULO,"Evolução ao longo do tempo");
                intent.putExtra(TIPO_GRAFICO,"line");
                intent.putExtra(DESCRICAO_GRAFICO,texto_descricao_grafico);
                startActivity(intent);
                return true;
            case R.id.menu_resumo:
                intent =  new Intent(this,ChartActivity.class);
                intent.putExtra(TIPO,"resumo");
                intent.putExtra(TITULO,"Dentre os confirmados:");
                intent.putExtra(TIPO_GRAFICO,"bar");
                startActivity(intent);
                return true;
            case R.id.menu_compartilhar_app:
                String conteudo = "https://play.google.com/store/apps/details?id=pet.yoko.apps.covid";
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, conteudo);
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, "Compartilhando dados...");
                startActivity(shareIntent);
                return true;
            case R.id.menu_pagina_app:
                String url = "https://github.com/rafaelperazzo/covid19_android_studio";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                return true;
            case R.id.menu_percentuais:
                if (getTitle().toString().contains("%")) {
                    setTitle("COVID19 APP");
                    try {
                        this.run(this.url,0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    int total = Integer.parseInt(this.confirmados.getText().toString());
                    double obitos = 0;
                    double recuperados = 0;
                    double emRecuperacao = 0;
                    try {
                        obitos = (Integer.parseInt(this.obitos.getText().toString())/(double)total)*100;
                        emRecuperacao = (Integer.parseInt(this.suspeitos.getText().toString())/(double)total)*100;
                    }
                    catch (ArithmeticException e) {
                        obitos = 0;
                        recuperados = 0;
                        emRecuperacao = 0;
                        e.printStackTrace();
                    }

                    DecimalFormat df = new DecimalFormat("#0.00");
                    this.obitos.setText(df.format(obitos) + "%");
                    this.suspeitos.setText(df.format(emRecuperacao) + "%");
                    setTitle("COVID19 APP (%)");
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void btnMapaBairrosClick(View view) {
        Intent intent = new Intent(this, TabelaActivity.class);
        startActivity(intent);

    }

    public void confirmadosClick(View view) {
        Intent intent;
        intent =  new Intent(this,ChartActivity.class);
        intent.putExtra(TIPO,"evolucao");
        intent.putExtra(CIDADE,cidade.getSelectedItem().toString());
        intent.putExtra(TITULO,"Evolução ao longo do tempo");
        intent.putExtra(TIPO_GRAFICO,"line");
        intent.putExtra(DESCRICAO_GRAFICO,texto_descricao_grafico);
        startActivity(intent);
    }

    void run(String url, final int tipo) throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progresso.setVisibility(View.GONE);
                        txtAvisos.setText("ERRO DE REDE. VERIFIQUE SUA CONEXÃO");
                    }
                });
                carregarDadosIniciais();
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (tipo==0) {
                            ajustarDadosIniciais(myResponse);
                        }
                        else if (tipo==1){
                            verificarAtualizacao(myResponse);
                        }
                        else {
                            ajustarAvisos(myResponse);
                        }
                    }
                });

            }
        });
    }

    public void ajustarAvisos(String myResponse) {
        if (myResponse.equals("")){
            txtAvisos.setVisibility(View.GONE);
        }
        else {
            txtAvisos.setVisibility(View.VISIBLE);
            txtAvisos.setText(myResponse);
        }
    }

    public void verificarAtualizacao(String myResponse) {
        int versaoNova = Ferramenta.getAppPlayStoreVersion(myResponse);
        if (VERSAO<versaoNova) {
            versao.setText("UMA NOVA VERSÃO ESTÁ DISPONÍVEL!");
            versao.setTextColor(Color.RED);
            atualizar.setVisibility(View.VISIBLE);
            try {
                popupAtualizar();
            }
            catch (IllegalStateException e) {
                Log.e("Exception: %s", e.getMessage());
            }

        }
    }

    public void ajustarDadosIniciais(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            String data_agora = obj.getString("atualizacao");
            String data_armazenada = getAtualizacao();
            if (data_agora.equals(data_armazenada)) {
                this.carregarDadosIniciais();
                progresso.setVisibility(View.GONE);
            }
            else {
                setAtualizacao(data_agora);
                DownloadData dd = new DownloadData(DatabaseClient.getInstance(getApplicationContext()).getAppDatabase(),progresso);
                Void retorno = dd.execute().get();
                this.carregarDadosIniciais();
                progresso.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void popupAtualizar() {
        try {
            DialogFragment newFragment = new AtualizarDialog();
            newFragment.show(getSupportFragmentManager(),"Atualizar");
        }
        catch (IllegalStateException e) {
            Log.e("Exception: %s", e.getMessage());
        }

    }
}
