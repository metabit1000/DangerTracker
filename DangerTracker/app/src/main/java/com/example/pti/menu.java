package com.example.pti;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.Contacts.People;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pti.Retrofit.MyService;
import com.example.pti.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class menu extends AppCompatActivity {
    Button logout;
    Button agregar;
    Button emergencia;
    TextView usuarioLogueado;
    TextView telfImportante;

    String usuari;

    public final int PICK_CONTACT = 2015;

    MyService myService;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().hide();

        //Init service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        myService = retrofitClient.create(MyService.class);

        logout =(Button)findViewById(R.id.logout);
        agregar = (Button) findViewById(R.id.button);
        emergencia= (Button) findViewById(R.id.emergencia);
        usuarioLogueado = (TextView) findViewById(R.id.UsuarioLogged);
        telfImportante = (TextView) findViewById(R.id.telf);

        Intent intent = getIntent();
        usuari = intent.getStringExtra(MapsActivity.user_i); //obtengo el usuario loggeado
        usuarioLogueado.setText(usuari); //pongo el usuario en pantalla

        compositeDisposable.add(myService.getContactos(usuari)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        String mensaje = adecuarMensaje(s);
                        if (mensaje.equals("No hay contactos")) telfImportante.setText("No tienes telefono de emergencia");
                        else {
                            JSONObject myJsonjObject = new JSONObject(mensaje);
                            String valor = myJsonjObject.getString("telf");
                            telfImportante.setText(valor);
                        }
                    }
                }));

        agregar.setOnClickListener(new View.OnClickListener() { //parte de acceder a los contactos
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(i, PICK_CONTACT);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (menu.this, MainActivity.class); //vuelvo a la primera clase
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });

        emergencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telfImportante.setText("112");
                //añadir a la base de datos
                compositeDisposable.add(myService.addContacto(usuari,"112")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                System.out.println(""+s);
                            }
                        }));
            }
        });
    }

    private String adecuarMensaje(String mensaje) { //adecuo el mensaje para JSON
        String cambiado= mensaje.substring(1,mensaje.length()-1); //quito los " inicial y final
        return cambiado.replaceAll("\\\\", ""); //quito las \ del mensaje
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        if (reqCode == PICK_CONTACT && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
            cursor.moveToFirst();
            int number = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String telf = cursor.getString(number);
            telfImportante.setText(telf);

            //añadir a la base de datos
            compositeDisposable.add(myService.addContacto(usuari,telf)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            System.out.println(""+s);
                        }
                    }));
        }
    }
}

