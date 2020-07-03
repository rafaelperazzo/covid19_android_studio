package pet.yoko.apps.covid;

import android.graphics.Color;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import java.util.ArrayList;

public class MyLineChart {

    private LineChart grafico;
    private ArrayList<Entry> dados;
    private ArrayList<Entry> dados2;
    private ArrayList<String> xLabels;
    private boolean labelsX;
    private String descricao;

    public ArrayList<Entry> getDados2() {
        return dados2;
    }

    public void setDados2(ArrayList<Entry> dados2) {
        this.dados2 = dados2;
    }

    public void setDados(ArrayList<Entry> dados) {
        this.dados = dados;
    }

    public void setxLabels(ArrayList<String> xLabels) {
        this.xLabels = xLabels;
    }

    public void setLabelsX(boolean labelsX) {
        this.labelsX = labelsX;
    }

    public MyLineChart(LineChart grafico,ArrayList<Entry> dados, ArrayList<String> xLabels, boolean labelsX) {
        this.grafico = grafico;
        this.dados = dados;
        this.dados2 = null;
        this.xLabels = xLabels;
        this.labelsX = labelsX;
    }

    public MyLineChart(LineChart grafico,ArrayList<Entry> dados, ArrayList<Entry> dados2, ArrayList<String> xLabels, boolean labelsX) {
        this.grafico = grafico;
        this.dados = dados;
        this.dados2 = dados2;
        this.xLabels = xLabels;
        this.labelsX = labelsX;
    }

    public MyLineChart(LineChart grafico,ArrayList<Entry> dados, ArrayList<Entry> dados2, ArrayList<String> xLabels, boolean labelsX,String descricao) {
        this.grafico = grafico;
        this.dados = dados;
        this.dados2 = dados2;
        this.xLabels = xLabels;
        this.labelsX = labelsX;
        this.descricao = descricao;
    }

    public void refresh() {
        this.grafico.invalidate();
    }

    public void makeChart() {
        LineDataSet lineDataSet = new LineDataSet(dados,"Confirmações");
        lineDataSet.setValueTextColor(Color.GREEN);
        lineDataSet.setColor(Color.GREEN);
        lineDataSet.setValueTextSize(20f);
        lineDataSet.setDrawValues(false);
        lineDataSet.setLineWidth(2);
        lineDataSet.setDrawCircles(false);
        LineData lineData = new LineData();
        lineData.addDataSet(lineDataSet);
        if (dados2!=null) {
            LineDataSet lineDataSet2 = new LineDataSet(dados2,"Óbitos");
            lineDataSet2.setValueTextColor(Color.RED);
            lineDataSet2.setValueTextSize(20f);
            lineDataSet2.setColor(Color.RED);
            lineDataSet2.setDrawValues(false);
            lineDataSet2.setLineWidth(2);
            lineDataSet2.setDrawCircles(false);
            lineData.addDataSet(lineDataSet2);
        }
        grafico.setData(lineData);
        grafico.animateY(2000);
        grafico.getXAxis().setEnabled(false);
        grafico.getDescription().setText("Um novo óbito (por 100.000 hab) a cada " + descricao + " dia(s)");
        grafico.getDescription().setTextSize(10);
        grafico.getDescription().setEnabled(true);
        grafico.invalidate();
        grafico.setSaveEnabled(true);
    }

}
