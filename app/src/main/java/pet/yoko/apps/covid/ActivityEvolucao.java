package pet.yoko.apps.covid;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    TextView ocupacaoUTI;
    TextView tendencia;
    TextView declive;
    LinearLayout layOutTendencia;
    ArrayList<String> labels;
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
        ocupacaoUTI = (TextView)findViewById(R.id.textOcupacaoUTIHoje);
        tendencia = (TextView)findViewById(R.id.textTendencia);
        layOutTendencia = (LinearLayout)findViewById(R.id.layoutTendencia);
        declive = (TextView)findViewById(R.id.textDeclive);
        this.setOcupacaoUTI(getIntent().getStringExtra("UTI"));
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
        //TaskGetEvolucaoMedia tem = new TaskGetEvolucaoMedia(DatabaseClient.getInstance(getApplicationContext()).getAppDatabase(),this,14,"TODAS AS CIDADES",1);
        //tem.execute();
        this.setTitle("INDICADORES");

    }

    private void setOcupacaoUTI(String valor) {
        try {
            int numero = Integer.parseInt(valor);
            if (numero<70) {
                ocupacaoUTI.setBackgroundColor(Color.GREEN);
            }
            else if ((numero>=70)&&(numero<80)) {
                ocupacaoUTI.setBackgroundColor(Color.YELLOW);
            }
            else if ((numero>=80)&&(numero<95)) {
                ocupacaoUTI.setBackgroundColor(Color.parseColor("#FF8000"));
            }
            else {
                ocupacaoUTI.setBackgroundColor(Color.RED);
            }
            ocupacaoUTI.setText(valor + "%");
        }
        catch (NumberFormatException e) {
            ocupacaoUTI.setText("ERRO");
        }
    }

    public void makeChart(ArrayList<Entry> dados, String dadosLabel, String descricao, int tipo) {
        //https://stackoverflow.com/questions/28960597/mpandroid-chart-how-to-make-smooth-line-chart

        LineDataSet lineDataSet = new LineDataSet(dados,dadosLabel);
        lineDataSet.setValueTextSize(12f);
        lineDataSet.setValueTextColor(Color.BLACK);
        lineDataSet.setDrawValues(true);
        lineDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return(String.format("%.1f",value));
                //return super.getFormattedValue(value);
            }
        });
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
        grafico.getAxisLeft().setDrawAxisLine(false);
        grafico.getXAxis().setGranularity(1f);
        grafico.getXAxis().setGranularityEnabled(true);
        grafico.getXAxis().setLabelCount(labels.size(),true);
        grafico.getXAxis().setLabelRotationAngle(-90);
        grafico.getAxisLeft().setDrawLabels(false);
        grafico.getAxisRight().setEnabled(false);
        grafico.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        grafico.getAxisLeft().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                DecimalFormat format = new DecimalFormat("#.#");
                return(format.format(value));
            }
        });
        //https://stackoverflow.com/questions/47637653/how-to-set-x-axis-labels-in-mp-android-chart-bar-graph
        grafico.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date d1;
                String valor = "";
                try {
                    d1 = df.parse(labels.get((int)value));
                    df.applyPattern("dd-MM-yyyy");
                    valor = df.format(d1);
                } catch (ParseException e) {
                    e.printStackTrace();
                    valor = "";
                }
                return (valor);
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
            if (dados.get(i).getY()<0) {
                cores.add(Color.GREEN);
            }
            else if (dados.get(i).getY()==0) {
                cores.add(Color.YELLOW);
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
            this.txtSituacao.setText("QUEDA/ESTABILIDADE NOS ÚLTIMOS " + periodo + " dias");
            this.txtSituacao.setBackgroundColor(Color.GREEN);
            this.txtSituacao.setTextColor(Color.BLACK);
        }
        else if (ultimo==0) {
            this.txtSituacao.setText("ESTABILIDADE NO PERÍODO NOS ÚLTIMOS " + periodo + " dias");
            this.txtSituacao.setBackgroundColor(Color.YELLOW);
            this.txtSituacao.setTextColor(Color.BLACK);
        }
        else {
            this.txtSituacao.setText("CRESCIMENTO/ESTABILIDADE NOS ÚLTIMOS " + periodo + " dias");
            this.txtSituacao.setBackgroundColor(Color.RED);
            this.txtSituacao.setTextColor(Color.WHITE);
        }
    }

    private void setTendencia(ArrayList<Entry> dados) {
        float somaX = 0;
        float somaY = 0;
        float somaXY = 0;
        float somaXX = 0;

        for (int i =0; i<dados.size(); i++) {
            somaX = somaX + dados.get(i).getX();
            somaY = somaY + dados.get(i).getY();
            somaXY = somaXY + (dados.get(i).getX()*dados.get(i).getY());
            somaXX = somaXX + (dados.get(i).getX()*dados.get(i).getX());
        }
        float a1 = ((dados.size())*somaXY-(somaX*somaY))/((dados.size()*somaXX)-(somaX*somaX));
        declive.setText(String.format("%.2f",a1));
        if (a1==0) {
            this.tendencia.setBackgroundColor(Color.YELLOW);
            this.layOutTendencia.setBackgroundColor(Color.YELLOW);
            this.tendencia.setTextColor(Color.BLACK);
            this.tendencia.setText("ESTABILIDADE");
        }
        else if (a1<0) {
            this.tendencia.setBackgroundColor(Color.GREEN);
            this.tendencia.setTextColor(Color.BLACK);
            this.tendencia.setText("DECRESCENTE");
            this.layOutTendencia.setBackgroundColor(Color.GREEN);
        }
        else {
            this.tendencia.setBackgroundColor(Color.RED);
            this.layOutTendencia.setBackgroundColor(Color.RED);
            this.tendencia.setTextColor(Color.WHITE);
            this.tendencia.setText("CRESCENTE");
        }

    }

    @Override
    public void getEvolucaoFinish(ArrayList<Entry> response, ArrayList<String> labels) {
        String dadosLabel;
        if (this.tipo==1) {
            dadosLabel = "Média Móvel - confirmações";
            this.setTendencia(response);
            this.layOutTendencia.setVisibility(View.VISIBLE);
        }
        else if (this.tipo==2) {
            dadosLabel = "Média Móvel - óbitos";
            this.setTendencia(response);
            this.layOutTendencia.setVisibility(View.VISIBLE);
        }
        else if (this.tipo==3) {
            dadosLabel = "Situação - confirmações";
            this.analisarDados(response);
            this.layOutTendencia.setVisibility(View.GONE);
        }
        else {
            dadosLabel = "Situacao - óbitos";
            this.analisarDados(response);
            this.layOutTendencia.setVisibility(View.GONE);
        }
        this.labels = labels;
        this.makeChart(response,dadosLabel,dadosLabel,this.tipo);
    }
}