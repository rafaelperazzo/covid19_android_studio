package pet.yoko.apps.covid;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import pet.yoko.apps.covid.db.DatabaseClient;
import pet.yoko.apps.covid.db.EvolucaoTotalItem;
import pet.yoko.apps.covid.db.TaskGetEvolucao;
import pet.yoko.apps.covid.db.TaskGetEvolucaoResponse;

public class ActivityEvolucao extends AppCompatActivity implements TaskGetEvolucaoResponse {
    LineChart grafico;
    RadioButton rbConfirmacoes;
    RadioButton rbObitos;
    List<EvolucaoTotalItem> dadosEvolucao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evolucao);
        grafico = (LineChart)findViewById(R.id.graficoMedia);
        rbConfirmacoes = (RadioButton)findViewById(R.id.rbConfirmacoes);
        rbObitos = (RadioButton)findViewById(R.id.rbObitos);
        TaskGetEvolucao tge = new TaskGetEvolucao(DatabaseClient.getInstance(getApplicationContext()).getAppDatabase(),this);
        tge.execute();
    }

    private void ajustarGrafico(int tipo) {
        ArrayList<Entry> dados = new ArrayList<>();
        for (int i=0; i<dadosEvolucao.size();i++) {
            if (tipo==2) {
                dados.add(new Entry(i,dadosEvolucao.get(i).getObitos()));
            }
            else {
                dados.add(new Entry(i,dadosEvolucao.get(i).getConfirmados()));
            }

        }
        if (tipo==1) {
            makeChart(dados,"Confirmações","Média Móvel",1);
        }
        else {
            makeChart(dados,"Óbitos","Média Móvel",2);
        }

    }

    @Override
    public void getEvolucaoFinish(List<EvolucaoTotalItem> response) {
        dadosEvolucao = response;
        ajustarGrafico(1);
    }

    public void makeChart(ArrayList<Entry> dados, String dadosLabel,String descricao,int tipo) {
        //grafico.invalidate();
        LineDataSet lineDataSet = new LineDataSet(dados,dadosLabel);
        if (tipo==1) {
            lineDataSet.setValueTextColor(Color.GREEN);
            lineDataSet.setColor(Color.GREEN);
        }
        else {
            lineDataSet.setValueTextColor(Color.RED);
            lineDataSet.setColor(Color.RED);
        }
        lineDataSet.setValueTextSize(20f);
        lineDataSet.setDrawValues(false);
        lineDataSet.setLineWidth(2);
        lineDataSet.setDrawCircles(false);
        LineData lineData = new LineData();
        lineData.addDataSet(lineDataSet);
        grafico.setData(lineData);
        grafico.animateY(2000);
        grafico.getXAxis().setEnabled(true);
        grafico.getAxisRight().setEnabled(false);
        grafico.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        grafico.getAxisLeft().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                DecimalFormat format = new DecimalFormat("#.#");
                return(format.format(value));
            }
        });
        grafico.getDescription().setText(descricao);
        grafico.getDescription().setTextSize(10);
        grafico.getDescription().setEnabled(true);
        grafico.invalidate();
        grafico.setSaveEnabled(true);
    }

    public void onRadioButtonClick(View v) {
        if (v.getId()==R.id.rbConfirmacoes) {
            this.ajustarGrafico(1);
        }
        else {
            this.ajustarGrafico(2);
        }
    }

}