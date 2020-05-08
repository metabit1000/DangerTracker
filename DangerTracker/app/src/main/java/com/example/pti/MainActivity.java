package com.example.pti;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pti.Retrofit.MyService;
import com.example.pti.Retrofit.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;



public class MainActivity extends AppCompatActivity {

    public final static String user_i = "com.example.pti.user_i";

    Button entra;
    Button registrarse;
    EditText email;
    String usuari;
    EditText password;
    MyService myService;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Vibrator vibrator;
    MapsActivity next = new MapsActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        myService = retrofitClient.create(MyService.class);

        //Init view
        email = (EditText)findViewById(R.id.name);
        password = (EditText)findViewById(R.id.password);
        entra =(Button)findViewById(R.id.entra);
        registrarse = (Button)findViewById(R.id.registrarse);

        entra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Execucio sense base de dades:
                  Intent i = new Intent (MainActivity.this, MapsActivity.class);
                  startActivity(i);
                  + comentar loginUser*/
                usuari = email.getText().toString();
                loginUser(email.getText().toString(), password.getText().toString());

            }
        });

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser(email.getText().toString(), password.getText().toString());
            }
        });
    }
    private void loginUser(final String email, String password) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(MainActivity.this, "El email no pot estar buit", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(MainActivity.this, "El password no pot estar buit", Toast.LENGTH_SHORT).show();
            return;
        }

        compositeDisposable.add(myService.loginUser(email,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if (s.equals("\"Login correcto\"")) {
                            Intent i = new Intent (MainActivity.this, MapsActivity.class);
                            i.putExtra(user_i,usuari); //paso el usuario loggeado correctamente
                            startActivity(i);
                        }
                        else
                            Toast.makeText(MainActivity.this, ""+s, Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    private void registerNewUser(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(MainActivity.this, "El email no pot estar buit", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(MainActivity.this, "El password no pot estar buit", Toast.LENGTH_SHORT).show();
            return;
        }

        compositeDisposable.add(myService.registerUser(email,"alex",password) //tengo que incluir name tambien...
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        String cambiado= s.substring(1,s.length()-1); //quito los " inicial y final
                        Toast.makeText(MainActivity.this, ""+cambiado, Toast.LENGTH_SHORT).show();
                    }
                }));
    }
}
