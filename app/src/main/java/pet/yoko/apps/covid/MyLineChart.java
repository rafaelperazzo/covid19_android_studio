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
    private ArrayList<String> xLabels;
    private boolean labelsX;

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
        this.xLabels = xLabels;
        this.labelsX = labelsX;
    }

    public void refresh() {
        this.grafico.invalidate();
    }

    public void makeChart() {
        LineDataSet lineDataSet = new LineDataSet(dados,"Datas");
        lineDataSet.setValueTextColor(Color.BLACK);
        lineDataSet.setValueTextSize(20f);
        LineData lineData = new LineData();
        lineData.addDataSet(lineDataSet);

        grafico.setData(lineData);
        /*
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
        */
        grafico.animateY(2000);
        //grafico.getDescription().setText(TITULO_GRAFICO);
        grafico.getDescription().setEnabled(false);
        grafico.invalidate();
        grafico.setSaveEnabled(true);
    }

}
