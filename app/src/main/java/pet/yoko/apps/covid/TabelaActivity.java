package pet.yoko.apps.covid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TabelaActivity extends AppCompatActivity {
    public String url = "https://apps.yoko.pet/api/covidapi.php";
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
        recyclerView = (RecyclerView)findViewById(R.id.tabela);
        items = new ArrayList<CidadeItem>();
        adapter = new CidadeAdapter(items);
        tools.prepararRecycleView(recyclerView,items,adapter);
        try {
            this.run();
        } catch (IOException e) {
            e.printStackTrace();
        }

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


    public void prepararRecycleView(RecyclerView recyclerView, ArrayList items, RecyclerView.Adapter adapter) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration itemDecor = new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
    }

    void run() throws IOException {
        progresso.setVisibility(View.VISIBLE);
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                progresso.setVisibility(View.GONE);
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();

                TabelaActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray arr = new JSONArray(myResponse);
                            TabelaCidades tabela = new TabelaCidades(arr,items,adapter);
                            tabela.makeTable();
                            progresso.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

}