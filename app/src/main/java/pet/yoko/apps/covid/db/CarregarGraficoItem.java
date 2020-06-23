package pet.yoko.apps.covid.db;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

import pet.yoko.apps.covid.MyBarChart;
import pet.yoko.apps.covid.MyLineChart;

public class CarregarGraficoItem extends AsyncTask<Void, Void, List<GraficoItem>> {

    private AppDatabase db;
    private ArrayList<BarEntry> valores_barra = new ArrayList<>();
    private ArrayList<Entry> valores_line = new ArrayList<>();
    ArrayList<Entry> valores_line2 = new ArrayList<>();
    private ArrayList<String> labels = new ArrayList<>();
    private String where;
    private String tipo;
    private BarChart grafico;
    private LineChart lineChart;
    private String DESCRICAO_GRAFICO;
    ProgressBar progresso;

    public CarregarGraficoItem(AppDatabase db,String where, String tipo, BarChart grafico, LineChart lineChart, String DESCRICAO_GRAFICO,ProgressBar progresso) {
        this.db = db;
        this.where = where;
        this.tipo = tipo;
        this.grafico = grafico;
        this.lineChart = lineChart;
        this.DESCRICAO_GRAFICO = DESCRICAO_GRAFICO;
        this.progresso = progresso;
        if (tipo.equals("bar")) {
            grafico.setVisibility(View.VISIBLE);
            lineChart.setVisibility(View.GONE);
        }
        else {
            grafico.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected List<GraficoItem> doInBackground(Void... voids) {
        this.progresso.setVisibility(View.VISIBLE);
        List<GraficoItem> items = db.graficoItemDao().getPorTipo(this.where);
        for (int i=0; i<items.size();i++) {
            labels.add(items.get(i).label);
            if (this.where.equals("evolucao")||this.where.equals("coeficiente")) {
                valores_line.add(new BarEntry(i,items.get(i).quantidade));
                valores_line2.add(new BarEntry(i,items.get(i).quantidade2));
            }
            else {
                valores_barra.add(new BarEntry(i,items.get(i).quantidade));
            }
        }
        this.progresso.setVisibility(View.GONE);
        return null;
    }

    @Override
    protected void onPostExecute(List<GraficoItem> graficoItems) {
        super.onPostExecute(graficoItems);
        if (tipo.equals("bar")) {
            MyBarChart chart = new MyBarChart(grafico,valores_barra,labels,true);
            chart.makeChart();
        }
        else {
            MyLineChart chart = new MyLineChart(lineChart,valores_line,valores_line2,labels,true,DESCRICAO_GRAFICO);
            chart.makeChart();
        }
    }
}
