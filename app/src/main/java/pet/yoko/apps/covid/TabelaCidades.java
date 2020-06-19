package pet.yoko.apps.covid;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TabelaCidades {

    private JSONArray obj;
    private ArrayList<CidadeItem> items;
    private RecyclerView.Adapter adapter;

    public TabelaCidades(JSONArray obj,ArrayList<CidadeItem> items,RecyclerView.Adapter adapter) {
        this.obj = obj;
        this.items = items;
        this.adapter = adapter;
    }

    public void makeTable() throws JSONException {

        items.clear();
        for (int i=0; i<obj.length();i++) {
            JSONObject linha = obj.getJSONObject(i);
            String cidade = linha.getString("cidade");
            int confirmados = linha.getInt("confirmados");
            int suspeitos = linha.getInt("suspeitos");
            int obitos = linha.getInt("obitos");
            double incidencia = linha.getDouble("taxa");
            int recuperados = linha.getInt("recuperados");
            int emRecuperacao = confirmados-obitos-recuperados;
            items.add(new CidadeItem(cidade,confirmados,suspeitos,obitos,incidencia,recuperados,emRecuperacao));
        }
        items.add(new CidadeItem("",-1,-1,-1,-1,-1,-1));

        Collections.sort(items, new Comparator<CidadeItem>() {
            @Override
            public int compare(CidadeItem o1, CidadeItem o2) {
                return (o1.cidade.compareTo(o2.cidade));
            }
        });

        adapter.notifyDataSetChanged();
    }

}
