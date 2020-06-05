package pet.yoko.apps.covid;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class MyBarChart {

    private BarChart grafico;
    private ArrayList<BarEntry> dados;
    private ArrayList<String> xLabels;
    private boolean labelsX;

    public void setDados(ArrayList<BarEntry> dados) {
        this.dados = dados;
    }

    public void setxLabels(ArrayList<String> xLabels) {
        this.xLabels = xLabels;
    }

    public void setLabelsX(boolean labelsX) {
        this.labelsX = labelsX;
    }

    public MyBarChart(BarChart grafico,ArrayList<BarEntry> dados, ArrayList<String> xLabels, boolean labelsX) {
        this.grafico = grafico;
        this.dados = dados;
        this.xLabels = xLabels;
        this.labelsX = labelsX;
    }

    public void refresh() {
        this.grafico.invalidate();
    }

    public void makeChart() {
        BarData barData = new BarData();
        for (int i = 0; i<dados.size();i++) {
            BarEntry linha = dados.get(i);
            String label = xLabels.get(i);
            ArrayList<BarEntry> data = new ArrayList<>();
            data.add(linha);
            BarDataSet barDataSet = new BarDataSet(data,label);
            //barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            Random rand = new Random();
            int r = rand.nextInt(254)+1;
            int g = rand.nextInt(254)+1;
            int b = rand.nextInt(254)+1;
            int randomColor = Color.rgb(r,g,b);
            barDataSet.setColors(randomColor);
            barDataSet.setValueTextColor(Color.BLACK);
            barDataSet.setValueTextSize(20f);
            barData.addDataSet(barDataSet);
        }

        barData.setBarWidth(1);
        grafico.setFitBars(true);
        grafico.setData(barData);
        Legend l = grafico.getLegend();
        l.setWordWrapEnabled(true);
        l.setMaxSizePercent(0.3f);
        l.setEnabled(true);
        XAxis xAxis = grafico.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setCenterAxisLabels(true);
        //xAxis.setDrawLabels(true);
        xAxis.setLabelCount(xLabels.size());
        xAxis.setGranularity(1.0f);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelRotationAngle(-90);
        xAxis.setDrawGridLines(false);
        xAxis.setCenterAxisLabels(true);
        xAxis.setAxisMinimum(0);
        xAxis.setEnabled(false);
        if (labelsX) {
            xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            l.setEnabled(false);
            xAxis.setEnabled(true);
            xAxis.setTextSize(20);
        }
        YAxis yAxis = grafico.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setAxisMinimum(0);
        yAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                DecimalFormat format = new DecimalFormat("#.####");
                return(format.format(value));
            }
        });
        yAxis.setGranularity(1.0f);
        yAxis.setGranularityEnabled(true);
        yAxis.setEnabled(false);
        //yAxis.setDrawLabels(true);
        grafico.getAxisRight().setDrawGridLines(false);
        grafico.getAxisRight().setEnabled(false);
        //grafico.getAxisRight().setDrawLabels(true);
        grafico.animateY(2000);
        grafico.setDrawBarShadow(false);
        //grafico.getDescription().setText(TITULO_GRAFICO);
        grafico.getDescription().setEnabled(false);
        if (dados.size()>1) {
            grafico.groupBars(0f,0f,0.06f);
        }
        grafico.invalidate();
        grafico.setSaveEnabled(true);

    }
}

