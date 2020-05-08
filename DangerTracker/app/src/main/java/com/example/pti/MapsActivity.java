package com.example.pti;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pti.Retrofit.MyService;
import com.example.pti.Retrofit.RetrofitClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.LinkedTransferQueue;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public final static String user_i = "com.example.pti.user_i";
    String usuari;

    private Vibrator vibrator;
    private GoogleMap mMap;
    Button menu;
    ImageButton sos;
    private Marker marcador;
    private Marker prova;
    double lat = 0.0;
    double lng = 0.0;
    LatLng planeta = new LatLng(0,0);
    private Location location;
    HashMap<Integer, LatLng> ubicaçaos = new HashMap<>();
    float distancia;
    Algoritme alg = new Algoritme();
    MediaPlayer mySong;

    MyService myService;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        Retrofit retrofitClient = RetrofitClient.getInstance();
        myService = retrofitClient.create(MyService.class);

        Intent intent = getIntent();
        usuari = intent.getStringExtra(MainActivity.user_i); //obtengo el usuario loggeado

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable((getApplicationContext()));
        if (status == ConnectionResult.SUCCESS) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, (Activity) getApplicationContext(), 10);
            dialog.show();
        }
        sos=(ImageButton)findViewById(R.id.SOS);

        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compositeDisposable.add(myService.getContactos(usuari)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                String mensaje = adecuarMensaje(s);
                                if (mensaje.equals("No hay contactos")) Toast.makeText(MapsActivity.this, "No tiene telfono de emergencia asignado", Toast.LENGTH_SHORT).show();
                                else {
                                    JSONObject myJsonjObject = new JSONObject(mensaje);
                                    String valor = myJsonjObject.getString("telf");
                                    truca_telefon(valor);
                                }
                            }
                        }));
            }
        });
        menu = (Button) findViewById(R.id.Menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MapsActivity.this, menu.class);
                i.putExtra(user_i,usuari);
                startActivity(i);
            }
        });

    }
    private void dibuixa_cercles() {
        double radi = 700.0;
        LatLng ravalLatLng = new LatLng (41.3798432, 2.1682149000000663);
        LatLng barcelonetaLatLng = new LatLng(41.3746936,2.172931);
        LatLng marinaLatLng = new LatLng(41.3604232,2.1317501);
        LatLng bordetaLatLng = new LatLng(41.3692989,2.1319386);
        LatLng besosLatLng = new LatLng(41.4147788,2.2085157);
        LatLng provençalsLatLng = new LatLng(41.416697,2.1941002);
        LatLng vernedaLatLng = new LatLng(41.4236105,2.1989811);
        LatLng prosperitatLatLng = new LatLng(41.4426765,2.176823);
        LatLng meridianaLatLng = new LatLng(41.4614629,2.1700352);
        LatLng pereLatLng = new LatLng(41.3865438,2.1788116);
        LatLng casaLatLng = new LatLng(41.426887, 1.788163);
        LatLng gelidaLatLng = new LatLng(41.440676, 1.866005);

        Date currentTime = Calendar.getInstance().getTime();
        String hora = currentTime.toString();
        String hora_propiament = hora.substring(11,13);
        String minuts_propiament = hora.substring(14,16);
        int hora_int = Integer.parseInt(hora_propiament);
        int minuts_int = Integer.parseInt(minuts_propiament);
        System.out.print("HORA: " + hora_int);
        System.out.print("MINUTS: " + minuts_int);


        if (hora_int >= 20 && (hora_int <= 6 && minuts_int <= 0)) {
            mMap.addCircle(new CircleOptions().center(ravalLatLng).radius(radi).strokeWidth(3f).strokeColor(Color.RED).fillColor(Color.argb(70, 150, 50, 50)));
            mMap.addCircle(new CircleOptions().center(barcelonetaLatLng).radius(radi).strokeWidth(3f).strokeColor(Color.RED).fillColor(Color.argb(70, 150, 50, 50)));
            mMap.addCircle(new CircleOptions().center(marinaLatLng).radius(radi).strokeWidth(3f).strokeColor(Color.RED).fillColor(Color.argb(70, 150, 50, 50)));
            mMap.addCircle(new CircleOptions().center(bordetaLatLng).radius(radi).strokeWidth(3f).strokeColor(0xFF8C00).fillColor(Color.argb(70, 255, 128, 0)));
            mMap.addCircle(new CircleOptions().center(besosLatLng).radius(radi).strokeWidth(3f).strokeColor(0xFFA8C00).fillColor(Color.argb(70, 255, 128, 0)));
            mMap.addCircle(new CircleOptions().center(provençalsLatLng).radius(radi).strokeWidth(3f).strokeColor(0xFF8C00).fillColor(Color.argb(70, 255, 128, 0)));
            mMap.addCircle(new CircleOptions().center(vernedaLatLng).radius(radi).strokeWidth(3f).strokeColor(Color.YELLOW).fillColor(Color.argb(70, 255, 255, 0)));
            mMap.addCircle(new CircleOptions().center(prosperitatLatLng).radius(radi).strokeWidth(3f).strokeColor(Color.YELLOW).fillColor(Color.argb(70, 255, 255, 0)));
            mMap.addCircle(new CircleOptions().center(meridianaLatLng).radius(radi).strokeWidth(3f).strokeColor(Color.YELLOW).fillColor(Color.argb(70, 255, 255, 0)));
            mMap.addCircle(new CircleOptions().center(pereLatLng).radius(radi).strokeWidth(3f).strokeColor(Color.YELLOW).fillColor(Color.argb(70, 255, 255, 0)));
            mMap.addCircle(new CircleOptions().center(casaLatLng).radius(radi).strokeWidth(3f).strokeColor(Color.YELLOW).fillColor(Color.argb(70, 255, 255, 0)));
            mMap.addCircle(new CircleOptions().center(gelidaLatLng).radius(radi).strokeWidth(3f).strokeColor(Color.YELLOW).fillColor(Color.argb(70, 255, 255, 0)));
        }
        else {
            mMap.addCircle(new CircleOptions().center(ravalLatLng).radius(radi).strokeWidth(3f).strokeColor(0xFF8C00).fillColor(Color.argb(70, 255, 128, 50)));
            mMap.addCircle(new CircleOptions().center(barcelonetaLatLng).radius(radi).strokeWidth(3f).strokeColor(0xFF8C00).fillColor(Color.argb(70, 255, 128, 50)));
            mMap.addCircle(new CircleOptions().center(marinaLatLng).radius(radi).strokeWidth(3f).strokeColor(0xFF8C00).fillColor(Color.argb(70, 255, 128, 50)));
            mMap.addCircle(new CircleOptions().center(bordetaLatLng).radius(radi).strokeWidth(3f).strokeColor(Color.YELLOW).fillColor(Color.argb(70, 255, 255, 0)));
            mMap.addCircle(new CircleOptions().center(besosLatLng).radius(radi).strokeWidth(3f).strokeColor(Color.YELLOW).fillColor(Color.argb(70, 255, 255, 0)));
            mMap.addCircle(new CircleOptions().center(provençalsLatLng).radius(radi).strokeWidth(3f).strokeColor(Color.YELLOW).fillColor(Color.argb(70, 255, 255, 0)));
            mMap.addCircle(new CircleOptions().center(vernedaLatLng).radius(radi).strokeWidth(3f).strokeColor(Color.GREEN).fillColor(Color.argb(70, 0, 255, 0)));
            mMap.addCircle(new CircleOptions().center(prosperitatLatLng).radius(radi).strokeWidth(3f).strokeColor(Color.GREEN).fillColor(Color.argb(70, 0, 255, 0)));
            mMap.addCircle(new CircleOptions().center(meridianaLatLng).radius(radi).strokeWidth(3f).strokeColor(Color.GREEN).fillColor(Color.argb(70, 0, 255, 0)));
            mMap.addCircle(new CircleOptions().center(pereLatLng).radius(radi).strokeWidth(3f).strokeColor(Color.GREEN).fillColor(Color.argb(70, 0, 255, 0)));
            mMap.addCircle(new CircleOptions().center(casaLatLng).radius(radi).strokeWidth(3f).strokeColor(Color.GREEN).fillColor(Color.argb(70, 255, 255, 0)));
            mMap.addCircle(new CircleOptions().center(gelidaLatLng).radius(radi).strokeWidth(3f).strokeColor(Color.GREEN).fillColor(Color.argb(70, 255, 255, 0)));
        }

    }
    private void truca_telefon(final String numero_telefon) {
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",numero_telefon,null)));
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        dibuixa_cercles();
        miUbicacion();
    }

    private void agregarMarcador(double lat, double lng) {
        LatLng coordenades = new LatLng(lat, lng);
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenades, 16);
        if (marcador != null) marcador.remove();
        int height = 100;
        int width = 80;
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.marcador03);
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
        marcador = mMap.addMarker(new MarkerOptions().position(coordenades).title("Mi Posicion").icon(smallMarkerIcon));
        mMap.animateCamera(miUbicacion);
    }

    private void actualizarUbicacion(Location location) {
        LatLng ubi = new LatLng(0,0);
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            agregarMarcador(lat,lng);
            ubi = new LatLng(lat,lng);
        }
        System.out.println("coordenades planeta: " + planeta);
        System.out.println("coordenades ubicacio: " + ubi);
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            actualizarUbicacion(location);
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            System.out.println("LA UBICACIO ESTA DEL REVES" + location.getLongitude() + " " + location.getLatitude());
            mySong = MediaPlayer.create(MapsActivity.this, R.raw.song);

            alg.core(mMap,location,vibrator, mySong);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void miUbicacion() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        System.out.println("UBI COORD: " + location.getLongitude() + " :" + location.getLatitude());

        actualizarUbicacion(location);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,0,locationListener);
    }

    private String adecuarMensaje(String mensaje) { //adecuo el mensaje para JSON
        String cambiado= mensaje.substring(1,mensaje.length()-1); //quito los " inicial y final
        return cambiado.replaceAll("\\\\", ""); //quito las \ del mensaje
    }
}

