package pet.yoko.apps.covid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import pet.yoko.apps.covid.db.CarregarEvolucaoTotal;
import pet.yoko.apps.covid.db.CarregarGraficoItem;
import pet.yoko.apps.covid.db.DatabaseClient;
import pet.yoko.apps.covid.db.EvolucaoTotalItem;

public class ChartActivity extends AppCompatActivity {

    ProgressBar progresso;
    BarChart grafico;
    LineChart lineChart;
    ImageView imagemGrafico;
    String TIPO_GRAFICO;
    String DESCRICAO_GRAFICO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        progresso = (ProgressBar)findViewById(R.id.progressoChart);
        progresso.setVisibility(View.VISIBLE);
        imagemGrafico = (ImageView)findViewById(R.id.imgCompartilharGrafico);
        grafico = (BarChart)findViewById(R.id.chart);
        lineChart = (LineChart)findViewById(R.id.lineChart);
        Intent intent = getIntent();
        String TITULO = intent.getStringExtra(MainActivity.TITULO);
        TIPO_GRAFICO = intent.getStringExtra(MainActivity.TIPO_GRAFICO);
        if (TIPO_GRAFICO.equals("line")) {
            DESCRICAO_GRAFICO = intent.getStringExtra(MainActivity.DESCRICAO_GRAFICO);
        }
        setTitle(TITULO);

        this.carregarDados();
    }

    public void carregarDados() {
        Intent intent = getIntent();
        String TIPO = intent.getStringExtra(MainActivity.TIPO);
        if (TIPO.equals("evolucao")) {
            String CIDADE = intent.getStringExtra(MainActivity.CIDADE);
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

    private void items2grafico(ArrayList<EvolucaoTotalItem> items) {
        ArrayList<Entry> valores_line = new ArrayList<>();
        ArrayList<Entry> valores_line2 = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        for (int i=0; i<items.size();i++) {
            labels.add(items.get(i).getData());
            valores_line.add(new Entry(i,items.get(i).getConfirmados()));
            valores_line2.add(new Entry(i,items.get(i).getObitos()));
        }
        grafico.setVisibility(View.GONE);
        lineChart.setVisibility(View.VISIBLE);
        progresso.setVisibility(View.GONE);
        MyLineChart chart = new MyLineChart(lineChart,valores_line,valores_line2,labels,true,DESCRICAO_GRAFICO);
        chart.makeChart();
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