package pet.yoko.apps.covid;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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

public class MapaActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String URL = "https://apps.yoko.pet/webapi/covidapi.php?dados=1&tipo=";
    ArrayList<String> cidades = new ArrayList<>();
    ArrayList<Integer> confirmados = new ArrayList<>();
    ArrayList<Double> latitude = new ArrayList<>();
    ArrayList<Double> longitude = new ArrayList<>();
    ArrayList<String> bairros = new ArrayList<>();
    String TIPO_MAPA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        String TIPO = intent.getStringExtra(MainActivity.TIPO_MAPA);
        this.TIPO_MAPA = TIPO;
        URL = URL + TIPO;
        try {
            this.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //https://www.javatpoint.com/android-google-map-displaying-current-location
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-7.2153453, -39.3153336);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(9.0f));

    }

    void run() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(URL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();

                MapaActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (TIPO_MAPA.equals("cidades")) {
                            setDadosCidades(myResponse);
                        } else {
                            setDadosBairros(myResponse);
                        }

                    }
                });

            }
        });
    }

    private void setDadosCidades(String myResponse) {
        try {
            JSONArray arr = new JSONArray(myResponse);
            cidades = new ArrayList<>();
            confirmados = new ArrayList<>();
            latitude = new ArrayList<>();
            longitude = new ArrayList<>();

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                cidades.add(obj.getString("cidade"));
                confirmados.add(obj.getInt("confirmados"));
                latitude.add(obj.getDouble("latitude"));
                longitude.add(obj.getDouble("longitude"));

                LatLng ponto = new LatLng(obj.getDouble("latitude"), obj.getDouble("longitude"));
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.virus26);
                mMap.addMarker(new MarkerOptions()
                        .position(ponto)
                        .title(obj.getString("cidade"))
                        .icon(icon)
                        .snippet("Confirmados: " + String.valueOf(obj.getInt("confirmados")))
                );

                CircleOptions circleOptions = new CircleOptions()
                        .center(ponto)
                        .fillColor(Color.LTGRAY)
                        .strokeColor(Color.TRANSPARENT)
                        .radius(30 * (int) obj.getDouble("incidencia"));
                mMap.addCircle(circleOptions);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setDadosBairros(String myResponse) {
        try {
            JSONArray arr = new JSONArray(myResponse);
            cidades = new ArrayList<>();
            bairros = new ArrayList<>();
            latitude = new ArrayList<>();
            longitude = new ArrayList<>();

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                cidades.add(obj.getString("cidade"));
                bairros.add(obj.getString("bairro"));
                latitude.add(obj.getDouble("latitude"));
                longitude.add(obj.getDouble("longitude"));

                LatLng ponto = new LatLng(obj.getDouble("latitude"), obj.getDouble("longitude"));
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.map30);
                mMap.addMarker(new MarkerOptions()
                        .position(ponto)
                        .title(obj.getString("cidade"))
                        .icon(icon)
                        .snippet("Bairro: " + String.valueOf(obj.getString("bairro")) + " - " + obj.getInt("confirmados") + " confirmado(s)")
                );

                CircleOptions circleOptions = new CircleOptions()
                        .center(ponto)
                        .fillColor(Color.LTGRAY)
                        .strokeColor(Color.RED)
                        .radius(250);
                mMap.addCircle(circleOptions);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}