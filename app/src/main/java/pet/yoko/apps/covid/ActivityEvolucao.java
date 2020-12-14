package pet.yoko.apps.covid;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.Spinner;

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
import pet.yoko.apps.covid.db.TaskGetEvolucaoMedia;
import pet.yoko.apps.covid.db.TaskGetEvolucaoMediaResponse;
import pet.yoko.apps.covid.db.TaskGetEvolucaoResponse;

public class ActivityEvolucao extends AppCompatActivity implements TaskGetEvolucaoMediaResponse {
    LineChart grafico;
    RadioButton rbConfirmacoes;
    RadioButton rbObitos;
    RadioButton rbMediaMovel;
    RadioButton rbSituacao;
    Spinner cmbCidade;
    Spinner cmbPeriodo;

    int tipo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evolucao);
        grafico = (LineChart)findViewById(R.id.graficoMedia);
        rbConfirmacoes = (RadioButton)findViewById(R.id.rbConfirmacoes);
        rbObitos = (RadioButton)findViewById(R.id.rbObitos);
        rbMediaMovel = (RadioButton)findViewById(R.id.rbMediaMovel);
        rbSituacao = (RadioButton)findViewById(R.id.rbSituacaoMedia);
        cmbCidade = (Spinner)findViewById(R.id.cmbCidadesGrafico);
        cmbPeriodo = (Spinner)findViewById(R.id.cmbPeriodoGrafico);
        cmbPeriodo.setSelection(1);
        cmbPeriodo.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                        ajustarDados();

                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

        cmbCidade.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                        ajustarDados();

                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

        this.tipo = 1;
        TaskGetEvolucaoMedia tem = new TaskGetEvolucaoMedia(DatabaseClient.getInstance(getApplicationContext()).getAppDatabase(),this,14,"TODAS AS CIDADES",1);
        tem.execute();
    }


    public void makeChart(ArrayList<Entry> dados, String dadosLabel,String descricao,int tipo) {
        grafico.invalidate();
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

    private void ajustarDados() {
        TaskGetEvolucaoMedia tem;
        String cidade = cmbCidade.getSelectedItem().toString();
        int periodo = Integer.parseInt(cmbPeriodo.getSelectedItem().toString());
        if ((rbMediaMovel.isChecked())&&(rbConfirmacoes.isChecked())) {
            tem = new TaskGetEvolucaoMedia(DatabaseClient.getInstance(getApplicationContext()).getAppDatabase(),this,periodo,cidade,1);
            this.tipo = 1;
        }
        else if ((rbMediaMovel.isChecked())&&(rbObitos.isChecked())) {
            tem = new TaskGetEvolucaoMedia(DatabaseClient.getInstance(getApplicationContext()).getAppDatabase(),this,periodo,cidade,2);
            this.tipo = 2;
        }
        else if ((rbSituacao.isChecked())&&(rbConfirmacoes.isChecked())) {
            tem = new TaskGetEvolucaoMedia(DatabaseClient.getInstance(getApplicationContext()).getAppDatabase(),this,periodo,cidade,3);
            this.tipo = 3;
        }
        else {
            tem = new TaskGetEvolucaoMedia(DatabaseClient.getInstance(getApplicationContext()).getAppDatabase(),this,periodo,cidade,4);
            this.tipo = 4;
        }
        tem.execute();
    }

    public void onRadioButtonClick(View v) {
        this.ajustarDados();
    }

    @Override
    public void getEvolucaoFinish(ArrayList<Entry> response) {
        String dadosLabel;
        if (this.tipo==1) {
            dadosLabel = "Média Móvel - confirmações";
        }
        else if (this.tipo==2) {
            dadosLabel = "Média Móvel - óbitos";
        }
        else if (this.tipo==3) {
            dadosLabel = "Situação - confirmações";
        }
        else {
            dadosLabel = "Situacao - óbitos";
        }
        this.makeChart(response,dadosLabel,dadosLabel,this.tipo);
    }
}