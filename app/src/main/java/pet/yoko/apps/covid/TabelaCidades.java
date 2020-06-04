package pet.yoko.apps.covid;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
            items.add(new CidadeItem(cidade,confirmados,suspeitos,obitos));
        }
        items.add(new CidadeItem("",-1,-1,-1));
        adapter.notifyDataSetChanged();
    }

}
