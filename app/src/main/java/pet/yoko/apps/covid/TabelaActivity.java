package pet.yoko.apps.covid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import pet.yoko.apps.covid.db.CarregarCidades;
import pet.yoko.apps.covid.db.DatabaseClient;

public class TabelaActivity extends AppCompatActivity {
    ProgressBar progresso;
    RecyclerView recyclerView;
    ArrayList<CidadeItem> items;
    CidadeAdapter adapter;
    SearchView pesquisar;
    ImageView share;
    Ferramenta tools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabela);
        tools = new Ferramenta(getApplicationContext());
        progresso = (ProgressBar)findViewById(R.id.progressoTabela);
        progresso.setVisibility(View.GONE);
        recyclerView = (RecyclerView)findViewById(R.id.tabela);
        items = new ArrayList<CidadeItem>();
        adapter = new CidadeAdapter(items);
        tools.prepararRecycleView(recyclerView,items,adapter);

        CarregarCidades cc = new CarregarCidades(DatabaseClient.getInstance(getApplicationContext()).getAppDatabase(),recyclerView);
        cc.execute();
        pesquisar = (SearchView)findViewById(R.id.search);
        pesquisar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                tools.filtrarTabela(items,adapter,query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                tools.filtrarTabela(items,adapter,newText);
                return false;
            }
        });
        share = (ImageView)findViewById(R.id.imgShare);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String conteudo = "";
                conteudo = tools.getTableText(recyclerView);
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, conteudo);
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, "Compartilhando dados...");
                startActivity(shareIntent);
            }
        });
    }

    public void cidadesClick(View v) {
        Collections.sort(items, new Comparator<CidadeItem>() {
            @Override
            public int compare(CidadeItem o1, CidadeItem o2) {
                return (o1.cidade.compareTo(o2.cidade));
            }
        });
        adapter.notifyDataSetChanged();
    }

    public void confirmadosClick(View v) {
        Collections.sort(items, new Comparator<CidadeItem>() {
            @Override
            public int compare(CidadeItem o1, CidadeItem o2) {
                return(o1.confirmados-o2.confirmados);
            }
        });
        adapter.notifyDataSetChanged();
        //Collections.sort(items,Comparator.comparing(CidadeItem::getConfirmados));
    }

    public void suspeitosClick(View v) {
        Collections.sort(items, new Comparator<CidadeItem>() {
            @Override
            public int compare(CidadeItem o1, CidadeItem o2) {
                return(o1.suspeitos-o2.suspeitos);
            }
        });
        adapter.notifyDataSetChanged();
    }

    public void obitosClick(View v) {
        Collections.sort(items, new Comparator<CidadeItem>() {
            @Override
            public int compare(CidadeItem o1, CidadeItem o2) {
                return(o1.obitos-o2.obitos);
            }
        });
        adapter.notifyDataSetChanged();
    }

    public void recuperadosClick(View v) {
        Collections.sort(items, new Comparator<CidadeItem>() {
            @Override
            public int compare(CidadeItem o1, CidadeItem o2) {
                return(o1.recuperados-o2.recuperados);
            }
        });
        adapter.notifyDataSetChanged();
    }

    public void emRecuperacaoClick(View v) {
        Collections.sort(items, new Comparator<CidadeItem>() {
            @Override
            public int compare(CidadeItem o1, CidadeItem o2) {
                return(o1.emRecuperacao-o2.emRecuperacao);
            }
        });
        adapter.notifyDataSetChanged();
    }

    public void incidenciaClick(View v) {
        Collections.sort(items, new Comparator<CidadeItem>() {
            @Override
            public int compare(CidadeItem o1, CidadeItem o2) {
                return (Double.compare(o1.incidencia,o2.incidencia));
            }
        });
        adapter.notifyDataSetChanged();
    }

}