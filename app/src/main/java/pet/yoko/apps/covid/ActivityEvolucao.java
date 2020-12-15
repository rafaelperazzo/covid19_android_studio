package pet.yoko.apps.covid;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

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
    TextView txtSituacao;
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
        txtSituacao = (TextView)findViewById(R.id.textSituacaoMediaMovel);
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
        this.setTitle("MÉDIA MÓVEL");
    }


    public void makeChart(ArrayList<Entry> dados, String dadosLabel,String descricao,int tipo) {
        //https://stackoverflow.com/questions/28960597/mpandroid-chart-how-to-make-smooth-line-chart
        grafico.invalidate();
        LineDataSet lineDataSet = new LineDataSet(dados,dadosLabel);
        lineDataSet.setValueTextSize(12f);
        lineDataSet.setValueTextColor(Color.BLACK);
        lineDataSet.setDrawValues(true);
        lineDataSet.setCubicIntensity(1);
        lineDataSet.resetColors();
        if (tipo==1) {
            lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            lineDataSet.setColor(Color.BLUE);
            lineDataSet.setCircleColor(Color.BLUE);
        }
        else if (tipo==2) {
            lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            lineDataSet.setColor(Color.RED);
            lineDataSet.setCircleColor(Color.RED);
        }
        if (tipo>2) {
            //lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            lineDataSet.resetColors();
            lineDataSet.setColors(this.ajustarCores(dados));
            lineDataSet.setCircleColors(this.ajustarCores(dados));
            lineDataSet.setDrawValues(false);
        }

        lineDataSet.setHighlightEnabled(true);
        lineDataSet.setLineWidth(2);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setCircleRadius(5);
        lineDataSet.setDrawCircleHole(false);
        LineData lineData = new LineData();
        lineData.addDataSet(lineDataSet);
        grafico.setData(lineData);
        grafico.animateY(2000);
        grafico.getXAxis().setEnabled(true);
        grafico.getAxisLeft().setDrawZeroLine(true);
        grafico.getAxisLeft().setZeroLineWidth(3);
        grafico.getAxisLeft().setZeroLineColor(Color.YELLOW);
        grafico.getAxisLeft().setDrawGridLines(false);
        grafico.getAxisLeft().setGranularity(1f);
        grafico.getAxisLeft().setGranularityEnabled(true);
        grafico.getXAxis().setDrawGridLines(false);
        grafico.getAxisLeft().setDrawAxisLine(true);
        grafico.getXAxis().setGranularity(1f);
        grafico.getXAxis().setLabelRotationAngle(-90);
        grafico.getAxisLeft().setDrawLabels(true);
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

    public List<Integer> ajustarCores(ArrayList<Entry> dados) {
        List<Integer> cores = new ArrayList<>();

        for (int i=0; i<dados.size();i++) {
            if (dados.get(i).getY()<=0) {
                cores.add(Color.GREEN);
            }
            else {
                cores.add(Color.RED);
            }
        }

        return (cores);
    }

    private void analisarDados(ArrayList<Entry> dados) {
        int ultimo = (int)dados.get(dados.size()-1).getY();
        int periodo = 1;
        for (int i=dados.size()-2;i>=0;i--) {
            if ((ultimo==(int)dados.get(i).getY())||((int)dados.get(i).getY()==0)) {
                periodo++;
            }
            else {
                break;
            }
        }
        if (ultimo==-1) {
            this.txtSituacao.setText("QUEDA/ESTABILIDADE NO PERÍODO DE " + periodo + " dias");
            this.txtSituacao.setBackgroundColor(Color.GREEN);
            this.txtSituacao.setTextColor(Color.BLACK);
        }
        else if (ultimo==0) {
            this.txtSituacao.setText("ESTABILIDADE NO PERÍODO DE " + periodo + " dias");
            this.txtSituacao.setBackgroundColor(Color.YELLOW);
            this.txtSituacao.setTextColor(Color.BLACK);
        }
        else {
            this.txtSituacao.setText("CRESCIMENTO/ESTABILIDADE NO PERÍODO DE " + periodo + " dias");
            this.txtSituacao.setBackgroundColor(Color.RED);
            this.txtSituacao.setTextColor(Color.WHITE);
        }
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
            this.analisarDados(response);
        }
        else {
            dadosLabel = "Situacao - óbitos";
            this.analisarDados(response);
        }
        this.makeChart(response,dadosLabel,dadosLabel,this.tipo);

    }
}