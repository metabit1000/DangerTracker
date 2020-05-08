package com.example.pti;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class principal extends AppCompatActivity {
        Button menu;
        Button sos;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.principal);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
          // setContentView(R.layout.activity_maps);
            menu=(Button)findViewById(R.id.Menu);
            sos=(Button)findViewById(R.id.SOS);

            sos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("hola");
                   // truca_telefon("670072561");
                }
            });

            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent (principal.this, menu.class);
                    System.out.println("holamenu");
                    //startActivity(i);
                }
            });

        }
       /* private void truca_telefon(final String numero_telefon) {
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",numero_telefon,null)));
        }*/
}


