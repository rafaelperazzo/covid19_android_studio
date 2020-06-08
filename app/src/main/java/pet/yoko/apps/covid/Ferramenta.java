package pet.yoko.apps.covid;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.inamik.text.tables.GridTable;
import com.inamik.text.tables.SimpleTable;
import com.inamik.text.tables.grid.Border;
import com.inamik.text.tables.grid.Util;
import java.util.ArrayList;
import static com.inamik.text.tables.Cell.Functions.VERTICAL_CENTER;

public class Ferramenta {

    private Context c;
    private ProgressBar progressoMain;

    public Ferramenta(Context c) {
        this.c = c;
    }

    public Ferramenta(Context c, ProgressBar pb) {
        this.c = c;
        this.progressoMain = pb;
    }

    public void prepararRecycleView(RecyclerView recyclerView, ArrayList items, RecyclerView.Adapter adapter) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(c);
        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration itemDecor = new DividerItemDecoration(c,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
    }


    public void filtrarTabela(ArrayList<CidadeItem> listaProjetos, CidadeAdapter adapter, String newText) {
        ArrayList<CidadeItem> filtrada = new ArrayList<>();
        if (!newText.isEmpty()) {
            for (CidadeItem linha: listaProjetos) {
                if (linha.cidade.toLowerCase().contains(newText)) {
                    filtrada.add(linha);
                }
            }
            filtrada.add((new CidadeItem("",-1,-1,-1,-1)));
            adapter.setItems(filtrada);
        }
        else {
            adapter.setItems(listaProjetos);
        }
        adapter.notifyDataSetChanged();
    }

    public String getTableText(RecyclerView tabela) {

        SimpleTable st = SimpleTable.of();
        st.nextRow();
        st.nextCell().addLine("Cidade");
        st.nextCell().addLine("Confirmados");
        st.nextCell().addLine("Suspeitos");
        st.nextCell().addLine("Óbitos");
        st.nextCell().addLine("Incidência");

        CidadeAdapter adapter = (CidadeAdapter)tabela.getAdapter();

        for (int i=0; i<adapter.getItemCount(); i++) {
            String cidade = adapter.getItems().get(i).cidade;
            int confirmados = adapter.getItems().get(i).confirmados;
            int suspeitos = adapter.getItems().get(i).suspeitos;
            int obitos = adapter.getItems().get(i).obitos;
            double incidencia = adapter.getItems().get(i).incidencia;
            st.nextRow();
            st.nextCell().addLine(cidade).applyToCell(VERTICAL_CENTER.withHeight(5));
            st.nextCell().addLine(String.valueOf(confirmados)).applyToCell(VERTICAL_CENTER.withHeight(5));
            st.nextCell().addLine(String.valueOf(suspeitos)).applyToCell(VERTICAL_CENTER.withHeight(5));
            st.nextCell().addLine(String.valueOf(obitos)).applyToCell(VERTICAL_CENTER.withHeight(5));
            st.nextCell().addLine(String.valueOf(incidencia)).applyToCell(VERTICAL_CENTER.withHeight(5));
        }

        GridTable gt = st.toGrid();
        gt = Border.of(Border.Chars.of('+', '-', '|')).apply(gt);
        return(Util.asString(gt));
    }

    public void shareTextContent(Context c,String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, "Compartilhando dados...");
        try {
            c.startActivity(shareIntent);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setImgProjetosShareClick(final Context c, final RecyclerView recyclerView,  ImageView share) {
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String conteudo = "";
                conteudo = getTableText(recyclerView);
                shareTextContent(c,conteudo);
            }
        });
    }

}
