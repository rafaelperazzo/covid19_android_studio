package pet.yoko.apps.covid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChartActivity extends AppCompatActivity {

    ProgressBar progresso;
    String URL;
    BarChart grafico;
    String TITULO_GRAFICO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        progresso = (ProgressBar)findViewById(R.id.progressoChart);
        progresso.setVisibility(View.VISIBLE);
        URL = "https://apps.yoko.pet/webapi/covidapi.php?dados=1&";
        grafico = findViewById(R.id.chart);
        Intent intent = getIntent();
        String TIPO = intent.getStringExtra(MainActivity.TIPO);
        String TITULO = intent.getStringExtra(MainActivity.TITULO);
        setTitle(TITULO);
        URL = URL + "tipo=" + TIPO;
        try {
            this.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shareClick(View view) {
        Bitmap image = grafico.getChartBitmap();
        Uri uri = null;
        try {
            File cachePath = new File(getApplicationContext().getCacheDir(), "images");
            cachePath.mkdirs();
            FileOutputStream stream = new FileOutputStream(cachePath + "/grafico.png");
            image.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.close();
            File imagePath = new File(getApplicationContext().getCacheDir(), "images");
            File newFile = new File(imagePath, "grafico.png");
            uri = FileProvider.getUriForFile(ChartActivity.this,BuildConfig.APPLICATION_ID + ".provider",newFile);
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("image/png");
            startActivity(Intent.createChooser(shareIntent, "Compartilhar"));
        } catch (IOException e) {

        }
    }

    void run() throws IOException {

        progresso.setVisibility(View.VISIBLE);
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(URL)
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

                ChartActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray arr = new JSONArray(myResponse);
                            ArrayList<BarEntry> valores = new ArrayList<>();
                            ArrayList<String> labels = new ArrayList<>();

                            for (int i=0;i<arr.length();i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                String label = obj.getString("label");
                                labels.add(label);
                                int quantidade = obj.getInt("quantidade");
                                valores.add(new BarEntry(i,quantidade));
                            }

                            progresso.setVisibility(View.GONE);
                            MyBarChart chart = new MyBarChart(grafico,valores,labels,true);
                            chart.makeChart();

                        } catch (JSONException e) {
                            progresso.setVisibility(View.GONE);
                            Toast toast = Toast.makeText(getApplicationContext(),"Erro no json: " + e.getMessage(),Toast.LENGTH_LONG);
                            toast.show();
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }
}