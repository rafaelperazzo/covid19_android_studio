package pet.yoko.apps.covid.db;

import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

public class CarregarDadosIniciais extends AsyncTask<Void, Void, List<DadosIniciais>> {

    private AppDatabase db;
    TextView confirmados;
    TextView emRecuperacao;
    TextView obitos;
    TextView comorbidades;
    TextView mediaObitosPorDia;
    TextView medianaIdade;
    //TextView uti;
    //TextView enfermaria;
    TextView atualizacao;

    public CarregarDadosIniciais(AppDatabase db, TextView confirmados, TextView emRecuperacao, TextView obitos,TextView atualizacao) {
        this.db = db;
        this.confirmados = confirmados;
        this.emRecuperacao = emRecuperacao;
        this.obitos = obitos;
        /*
        //this.comorbidades = comorbidades;
        //this.mediaObitosPorDia = mediaObitosPorDia;
        //this.medianaIdade = medianaIdade;
        //this.uti = uti;
        //this.enfermaria = enfermaria;*/
        this.atualizacao = atualizacao;
    }

    @Override
    protected List<DadosIniciais> doInBackground(Void... voids) {
        List<DadosIniciais> items = db.dadosIniciaisDao().getAll();
        return items;
    }

    @Override
    protected void onPostExecute(List<DadosIniciais> items) {
        super.onPostExecute(items);
        try {
            DecimalFormat df = new DecimalFormat("#0.00");
            this.confirmados.setText(String.valueOf(items.get(0).getConfirmados()));
            this.emRecuperacao.setText(String.valueOf(items.get(0).getEmRecuperacao()));
            this.obitos.setText(String.valueOf(items.get(0).getObitos()));
            //this.comorbidades.setText(items.get(0).getComorbidades());
            //this.mediaObitosPorDia.setText(String.valueOf(items.get(0).getMediaObitosPorDia()));
            //this.medianaIdade.setText(String.valueOf(items.get(0).getMedianaIdade()));
            //this.uti.setText(items.get(0).getUti() + "%");
            //this.enfermaria.setText(items.get(0).getEnfermaria() + "%");
            int uti = items.get(0).getUti();
            int enfermaria = items.get(0).getEnfermaria();
            /*if (uti>85) {
                this.uti.setBackgroundColor(Color.parseColor("#660000"));
                this.uti.setTextColor(Color.WHITE);
            }
            else {
                this.uti.setBackgroundColor(Color.parseColor("#009900"));
                this.uti.setTextColor(Color.WHITE);
            }
            if (enfermaria>70) {
                this.uti.setBackgroundColor(Color.parseColor("#660000"));
                this.uti.setTextColor(Color.WHITE);
            }
            else {
                this.enfermaria.setBackgroundColor(Color.parseColor("#009900"));
                this.enfermaria.setTextColor(Color.WHITE);
            }*/

            this.atualizacao.setText(items.get(0).getAtualizacao());
        }
        catch (IndexOutOfBoundsException e) {
            this.confirmados.setText("0");
            this.emRecuperacao.setText("0");
            this.obitos.setText("0");
            //this.comorbidades.setText("0");
            //this.mediaObitosPorDia.setText("0");
            //this.medianaIdade.setText("0");
            //this.uti.setText("0");
            //this.enfermaria.setText("0");
            this.atualizacao.setText("0");
        }

    }
}
