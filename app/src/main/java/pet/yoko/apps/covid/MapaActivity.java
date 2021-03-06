package pet.yoko.apps.covid;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.util.List;
import java.util.concurrent.ExecutionException;
import pet.yoko.apps.covid.db.BairroMapaItem;
import pet.yoko.apps.covid.db.CarregarDadosMapa;
import pet.yoko.apps.covid.db.CarregarDadosMapaBairros;
import pet.yoko.apps.covid.db.CidadeMapaItem;
import pet.yoko.apps.covid.db.DatabaseClient;

public class MapaActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String URL = "https://sci02-ter-jne.ufca.edu.br/webapi/covidapi.php?dados=1&tipo=";
    String TIPO_MAPA;
    FusedLocationProviderClient mFusedLocationProviderClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    private static final String TAG = MapaActivity.class.getSimpleName();
    private final LatLng mDefaultLocation = new LatLng(-7.2153453, -39.3153336);
    private Marker minhaLocalizacao;

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
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        updateLocationUI();
        getDeviceLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLocationUI();
        getDeviceLocation();
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
        if (TIPO_MAPA.equals("cidades")) {
            mMap.animateCamera(CameraUpdateFactory.zoomTo(9));
        }
        else {
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }
        updateLocationUI();
        getDeviceLocation();
        this.iniciar();
    }

    private void iniciar() {
        MapaActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (TIPO_MAPA.equals("cidades")) {
                    CarregarDadosMapa cdm = new CarregarDadosMapa(DatabaseClient.getInstance(getApplicationContext()).getAppDatabase());
                    try {
                        List<CidadeMapaItem> cidades = cdm.execute().get();
                        setDadosCidades(cidades);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    CarregarDadosMapaBairros cdmb = new CarregarDadosMapaBairros(DatabaseClient.getInstance(getApplicationContext()).getAppDatabase());
                    try {
                        List<BairroMapaItem> bairros = cdmb.execute().get();
                        setDadosBairros(bairros);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    private void setDadosCidades(List<CidadeMapaItem> cidades) {
        try {
            for (int i = 0; i < cidades.size(); i++) {
                CidadeMapaItem cidade = cidades.get(i);
                LatLng ponto = new LatLng(cidade.getLatitude(), cidade.getLongitude());
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.covid32);
                String snippet = "Confirmados: " + String.valueOf(cidade.getConfirmados());
                snippet = snippet + "\n" + "Recuperados: " + String.valueOf(cidade.getRecuperados());
                snippet = snippet + "\n" + "Óbitos: " + String.valueOf(cidade.getObitos());
                snippet = snippet + "\n" + "Em recuperação: " + String.valueOf(cidade.getEmRecuperacao());
                mMap.addMarker(new MarkerOptions()
                        .position(ponto)
                        .title(cidade.getCidade())
                        .icon(icon)
                        .snippet(snippet)
                );
                this.ajustarInformacoes();
                CircleOptions circleOptions = new CircleOptions()
                        .center(ponto)
                        .fillColor(Color.YELLOW)
                        .strokeColor(Color.TRANSPARENT)
                        .radius(30 * (int) cidade.getEmRecuperacao());
                mMap.addCircle(circleOptions);

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDadosBairros(List<BairroMapaItem> bairros) {
        //https://www.flaticon.com/
        try {

            for (int i = 0; i < bairros.size(); i++) {
                BairroMapaItem bairro = bairros.get(i);
                LatLng ponto = new LatLng(bairro.getLatitude(), bairro.getLongitude());
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.bairro32);
                mMap.addMarker(new MarkerOptions()
                        .position(ponto)
                        .title(bairro.getCidade())
                        .icon(icon)
                        .snippet("Bairro: " + bairro.getBairro() + " - " + bairro.getConfirmados() + " confirmado(s)")
                );
                int confirmados = bairro.getConfirmados();
                CircleOptions circleOptions = new CircleOptions()
                        .center(ponto)
                        .fillColor(Color.parseColor("#33FFFF"))
                        .strokeColor(Color.parseColor("#33FFFF"))
                        .radius(5*confirmados);
                mMap.addCircle(circleOptions);

            }
        }
        catch (Exception e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            try {
                                // Set the map's camera position to the current location of the device.
                                mLastKnownLocation = (Location) task.getResult();
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), 12));
                                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.user48);
                                if (minhaLocalizacao!=null) {
                                    minhaLocalizacao.remove();
                                }
                                minhaLocalizacao = mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude()))
                                        .icon(icon)
                                        .title("Você está aqui!")
                                        .snippet("Sua Localização!")
                                );
                                minhaLocalizacao.showInfoWindow();
                                if (TIPO_MAPA.equals("cidades")) {
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude()), 12));
                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(9));
                                }
                                else {
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude()), 11));
                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                                }
                            }
                            catch (NullPointerException e) {

                            }
                        }
                        else {
                            Log.d(TAG, "current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, 9));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        try {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }
        catch (Exception e) {
            Log.e("Exception: %s", e.getMessage());
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        try {
            mLocationPermissionGranted = false;
            switch (requestCode) {
                case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        mLocationPermissionGranted = true;
                    }
                }
            }
            updateLocationUI();
        }
        catch (Exception e) {
            Log.e("Exception: %s", e.getMessage());
        }

    }


    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        }
        catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
        catch (Exception e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void ajustarInformacoes() {
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(getApplicationContext());
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(getApplicationContext());
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(getApplicationContext());
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
    }

}