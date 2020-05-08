package com.example.pti;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;

public class Algoritme extends Service{
    private static final int NOTIF_ID = 1;
    private static final String NOTIF_CHANNEL_ID = "Channel_Id";
    //https://www.youtube.com/watch?v=pSMa19vy8D8
    //https://www.youtube.com/watch?v=wRDLjUK8nyU

    Vibrator vibrator;
    HashMap<Double, Location> ubicaçaos = new HashMap<>();
    Context mContext;
    boolean missatge = true;
    public void core(GoogleMap mMap,  Location Mylocation, Vibrator vibrator, MediaPlayer mySong) {

//Inicialitzacio
       /* Location aux = new Location("JO");
        aux.setLongitude(41.3768432);
        aux.setLatitude(2.1632149000000663);
        Mylocation = aux;*/


        Location raval = new Location ("Raval");
        raval.setLongitude(41.3798432);
        raval.setLatitude(2.1682149000000663);

        Location barceloneta = new Location("Barceloneta");
        barceloneta.setLongitude( 41.3746936);
        barceloneta.setLatitude(2.172931);

        Location marina_port = new Location("Marina");
        marina_port.setLongitude(41.3604232);
        marina_port.setLatitude(2.1317501);

        Location bordeta = new Location("Bordeta");
        bordeta.setLongitude(41.3692989);
        bordeta.setLatitude(2.1319386);

        Location besos = new Location("Besos");
        besos.setLongitude(41.4147788);
        besos.setLatitude(2.2085157);

        Location provençals = new Location("Provençals");
        provençals.setLongitude(41.416697);
        provençals.setLatitude(2.1941002);

        Location verneda = new Location("Verneda");
        verneda.setLatitude(41.4236105);
        verneda.setLongitude(2.1989811);

        Location prosperitat = new Location("Prosperitat");
        prosperitat.setLongitude(41.4426765);
        prosperitat.setLatitude(2.176823);

        Location meridiana = new Location("Meridiana");
        meridiana.setLongitude(41.4614629);
        meridiana.setLatitude(2.1700352);

        Location pere = new Location("Pere");
        pere.setLongitude(41.3865438);
        pere.setLatitude(2.1788116);

        Location casa = new Location("casa");
        casa.setLongitude(41.426887);
        casa.setLatitude(1.788163);

        Location gelida = new Location ("Gelida");
        gelida.setLongitude(41.440676);
        gelida.setLatitude(1.866005);

        double l_raval = raval.getLongitude();
        double l_barceloneta = barceloneta.getLongitude();
        double l_marina_port = marina_port.getLongitude();
        double l_bordeta = bordeta.getLongitude();
        double l_besos = besos.getLongitude();
        double l_provençals = provençals.getLongitude();
        double l_verneda = verneda.getLongitude();
        double l_prosperitat = prosperitat.getLongitude();
        double l_meridiana = meridiana.getLongitude();
        double l_pere = pere.getLongitude();
        double l_casa = casa.getLongitude();
        double l_gelida = gelida.getLongitude();

        ubicaçaos.put(l_raval,raval);
        ubicaçaos.put(l_barceloneta,barceloneta);
        ubicaçaos.put(l_marina_port,marina_port);
        ubicaçaos.put(l_bordeta,bordeta);
        ubicaçaos.put(l_besos,besos);
        ubicaçaos.put(l_provençals,provençals);
        ubicaçaos.put(l_verneda,verneda);
        ubicaçaos.put(l_prosperitat,prosperitat);
        ubicaçaos.put(l_meridiana,meridiana);
        ubicaçaos.put(l_pere,pere);
        ubicaçaos.put(l_casa,casa);
        ubicaçaos.put(l_gelida,gelida);

//Dibuixem en el mapa


//Calcul algortime

        Boolean estemDins = false;
        Boolean Dinslongitude= false;
        Boolean Dinslatitude= false;


        double myLocationLongitude = Mylocation.getLatitude();
        double myLocationLatitude = Mylocation.getLongitude();

        System.out.println("UBICACIÓ: "+ myLocationLongitude + myLocationLatitude);
        for(HashMap.Entry<Double, Location> entry : ubicaçaos.entrySet()) {

            Double x1 = entry.getValue().getLongitude()  + 0.007;
            Double x2 = entry.getValue().getLongitude()  - 0.007;

            Double y1 = entry.getValue().getLatitude()  + 0.007;
            Double y2 = entry.getValue().getLatitude()  - 0.007;


            System.out.println("Locatitzacio Raval amb rang: (" + x1 + " , " +  x2 + ")");
            System.out.println("La meva Locatitzacio: (" + myLocationLongitude + " " + myLocationLatitude +" )");

            //check Longitude
            if (x1 >= myLocationLongitude && x2 <= myLocationLongitude) {
                Dinslongitude = true;
                System.out.println("Primera Condicio");
            }

            //check Latitude
            if (y1 >= myLocationLatitude && y2 <= myLocationLatitude) {
                Dinslatitude = true;
                System.out.println("Segona Condicio");
            }
        }

//check if we are in
        if (Dinslongitude && Dinslatitude) estemDins = true;

//si estem dins, vibra

        if (estemDins)
        {
            //vibration
            if (missatge) {
                mySong.start();
                vibrator.vibrate(1000);
                //missatge = false;
            }

            System.out.println("Vibration ¬¬");
        }
        else {
            System.out.println("NO ENTRA");
        }

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground();
        return super.onStartCommand(intent, flags, startId);
    }
    private void startForeground() {
        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        startForeground(NOTIF_ID, new NotificationCompat.Builder(this,
                NOTIF_CHANNEL_ID) // don't forget create a notification channel first
                .setOngoing(true)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Service is running background")
                .setContentIntent(pendingIntent)
                .build());
    }
}