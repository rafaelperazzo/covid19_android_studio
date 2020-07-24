package pet.yoko.apps.covid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

import pet.yoko.apps.covid.db.AppDatabase;
import pet.yoko.apps.covid.db.CarregarEvolucaoTotal;
import pet.yoko.apps.covid.db.CarregarGraficoItem;
import pet.yoko.apps.covid.db.DatabaseClient;
import pet.yoko.apps.covid.db.EvolucaoTotalItem;
import pet.yoko.apps.covid.db.GetPopulacao;

public class ChartActivity extends AppCompatActivity {

    ProgressBar progresso;
    BarChart grafico;
    LineChart lineChart;
    ImageView imagemGrafico;
    String TIPO_GRAFICO;
    String DESCRICAO_GRAFICO;
    TextView textSituacao;
    TextView textObitos;
    ImageView imgChartConfirmacoes;
    ImageView imgChartObitos;
    ImageView imgChartCurva;
    ImageView imgChartAjuda;
    LinearLayout lConfirmacoes;
    LinearLayout lObitos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        progresso = (ProgressBar)findViewById(R.id.progressoChart);
        progresso.setVisibility(View.VISIBLE);
        imagemGrafico = (ImageView)findViewById(R.id.imgCompartilharGrafico);
        grafico = (BarChart)findViewById(R.id.chart);
        lineChart = (LineChart)findViewById(R.id.lineChart);
        textSituacao = (TextView)findViewById(R.id.textChartSituacao);
        textObitos = (TextView)findViewById(R.id.textChartObitos);
        imgChartConfirmacoes = (ImageView) findViewById(R.id.imgChartConfirmacoes);
        imgChartObitos = (ImageView)findViewById(R.id.imgChartObitos);
        imgChartCurva = (ImageView)findViewById(R.id.imgChartCurva);
        imgChartCurva.setVisibility(View.GONE);
        imgChartAjuda = (ImageView)findViewById(R.id.imgChartAjuda);
        lConfirmacoes = (LinearLayout)findViewById(R.id.layoutChartConfirmacoes);
        lObitos = (LinearLayout)findViewById(R.id.layoutChartObitos);
        Intent intent = getIntent();
        String TITULO = intent.getStringExtra(MainActivity.TITULO);
        TIPO_GRAFICO = intent.getStringExtra(MainActivity.TIPO_GRAFICO);
        if (TIPO_GRAFICO.equals("line")) {
            DESCRICAO_GRAFICO = intent.getStringExtra(MainActivity.DESCRICAO_GRAFICO);
        }
        else {
            lConfirmacoes.setVisibility(View.GONE);
            lObitos.setVisibility(View.GONE);
            imgChartAjuda.setVisibility(View.GONE);
            imagemGrafico.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
        }
        setTitle(TITULO);

        this.carregarDados();
    }

    public void carregarDados() {
        Intent intent = getIntent();
        String TIPO = intent.getStringExtra(MainActivity.TIPO);
        if (TIPO.equals("evolucao")) {
            String CIDADE = intent.getStringExtra(MainActivity.CIDADE);
            setTitle(CIDADE);
            CarregarEvolucaoTotal cet;
            if (CIDADE.equals("TODAS AS CIDADES")) {
                cet = new CarregarEvolucaoTotal(DatabaseClient.getInstance(getApplicationContext()).getAppDatabase(),"GERAL","TODAS");
            }
            else {
                cet = new CarregarEvolucaoTotal(DatabaseClient.getInstance(getApplicationContext()).getAppDatabase(),"GERAL",CIDADE);
            }
            try {
                ArrayList<EvolucaoTotalItem> items = (ArrayList<EvolucaoTotalItem>)cet.execute().get();
                items2grafico(items);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else {
            CarregarGraficoItem cgi = new CarregarGraficoItem(DatabaseClient.getInstance(getApplicationContext()).getAppDatabase(),TIPO,TIPO_GRAFICO,grafico,lineChart,DESCRICAO_GRAFICO,progresso);
            cgi.execute();
        }
    }

    private double mediaMovel(int posicao,ArrayList<EvolucaoTotalItem> items,int tipo) {
        double media = 0;
        int dias = 0;
        if (posicao-6>0) {
            for (int i=posicao;i>=posicao-6;i--) {
                try {
                    if (tipo==0) {
                        media = media + items.get(i).getConfirmados()-items.get(i-1).getConfirmados();
                    }
                    else {
                        media = media + items.get(i).getObitos()-items.get(i-1).getObitos();
                    }
                }
                catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                dias++;
            }
            media = media/((double)dias);
        }

        return (media);
    }

    private void items2grafico(ArrayList<EvolucaoTotalItem> items) {
        ArrayList<Entry> valores_line = new ArrayList<>();
        ArrayList<Entry> valores_line2 = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        double media_movel_confirmados = 0;
        double media_movel_obitos = 0;
        int dia = 1;
        for (int i=items.size()-1; i>0;i--) {
            media_movel_confirmados = 0;
            media_movel_obitos = 0;
            try {
                media_movel_confirmados = media_movel_confirmados + mediaMovel(i,items,0);
                media_movel_obitos = media_movel_obitos + mediaMovel(i,items,1);
                valores_line.add(new Entry(dia,(float)media_movel_confirmados));
                valores_line2.add(new Entry(dia,(float)media_movel_obitos));
                labels.add(String.valueOf(dia));
                dia++;
            }
            catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }

        }
        Collections.reverse(labels);
        ArrayList<Entry> confirmados = ordenarLista(valores_line);
        ArrayList<Entry> obitos = ordenarLista(valores_line2);
        double hoje = 0;
        double ontem = 0;
        try {
            hoje = confirmados.get(confirmados.size()-1).getY();
            ontem = confirmados.get(confirmados.size()-1-14).getY();
            setSituacao(hoje,ontem,textSituacao,0);
            hoje = obitos.get(obitos.size()-1).getY();
            ontem = obitos.get(obitos.size()-1-14).getY();
            setSituacao(hoje,ontem,textObitos,1);
        }
        catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        grafico.setVisibility(View.GONE);
        lineChart.setVisibility(View.VISIBLE);
        progresso.setVisibility(View.GONE);
        MyLineChart chart = new MyLineChart(lineChart,confirmados,obitos,labels,true,DESCRICAO_GRAFICO);
        chart.makeChart();
    }

    private void setSituacao(double hoje, double ontem, TextView textSituacao, int tipo) {
        double diferenca = hoje-ontem;
        if ((diferenca/ontem)>0.15) {
            //CRESCIMENTO
            textSituacao.setText("CRESCIMENTO");
            textSituacao.setBackgroundColor(Color.RED);
            if (tipo==1) {
                imgChartObitos.setImageResource(R.drawable.increase);
            }
            else {
                imgChartConfirmacoes.setImageResource(R.drawable.increase);
            }

        }
        else if ((diferenca/ontem)<-0.15) {
            textSituacao.setText("QUEDA       ");
            textSituacao.setBackgroundColor(Color.GREEN);
            if (tipo==1) {
                imgChartObitos.setImageResource(R.drawable.decrease);
            }
            else {
                imgChartConfirmacoes.setImageResource(R.drawable.decrease);
            }
        }
        else {
            //ESTABILIDADE
            textSituacao.setText("ESTABILIDADE");
            textSituacao.setBackgroundColor(Color.YELLOW);
            if (tipo==1) {
                imgChartObitos.setImageResource(R.drawable.estabilidade);
            }
            else {
                imgChartConfirmacoes.setImageResource(R.drawable.estabilidade);
            }
        }
    }

    public void ajudaClick(View v) {
        if (TIPO_GRAFICO.equals("line")) {
            if (imgChartCurva.getVisibility()==View.GONE) {
                imgChartCurva.setVisibility(View.VISIBLE);
                lineChart.setVisibility(View.GONE);
            }
            else {
                imgChartCurva.setVisibility(View.GONE);
                lineChart.setVisibility(View.VISIBLE);
            }
        }

    }

    public ArrayList<Entry> ordenarLista(ArrayList<Entry> lista) {
        ArrayList<Entry> retorno = new ArrayList<>();
        int dia = 1;
        for (int i=lista.size()-1;i>=0;i--) {
            retorno.add(new Entry(dia,lista.get(i).getY()));
            dia++;
        }
        return(retorno);
    }

    public void shareClick(View view) {
        Bitmap image;
        if (TIPO_GRAFICO.equals("bar")) {
            image = grafico.getChartBitmap();
        }
        else {
            image = lineChart.getChartBitmap();
        }

        Uri uri = null;
        try {
            File cachePath = new File(getApplicationContext().getCacheDir(), "images");
            cachePath.mkdirs();
            FileOutputStream stream = new FileOutputStream(cachePath + "/grafico.png");
            image.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.close();
            File imagePath = new File(getApplicationContext().getCacheDir(), "images");
            File newFile = new File(imagePath, "grafico.png");
            uri = FileProvider.getUriForFile(ChartActivity.this,BuildConfig.APPLICATION_ID + ".provider",newFile);
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("image/png");
            startActivity(Intent.createChooser(shareIntent, "Compartilhar"));
        } catch (IOException e) {

        }
    }


}