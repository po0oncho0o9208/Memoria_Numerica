package com.gametops.memorianumerica;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class Trofeos extends AppCompatActivity implements View.OnClickListener {
    GridView grilla;
    Sonido click;
    TextView txtest, txtgriditem;
    SharedPreferences sharedPref;
    ImageView btnatras;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trofeos);

        click = new Sonido(R.raw.click, this);
        grilla = findViewById(R.id.grilla);
        txtest = findViewById(R.id.txtestrella);
        txtgriditem = findViewById(R.id.txttitulo);
        btnatras = findViewById(R.id.atras);
        sharedPref = getSharedPreferences("record", Context.MODE_PRIVATE);


        int contador = 0;

        for (int n = 0; n < 4; n++) {
            for (int i = 0; i < 27; i++) {
                contador += sharedPref.getInt(n + "record" + i, 0);
            }
        }
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/birdyame.ttf");

        txtest.setText(contador + " X ");

        txtest.setTypeface(font);

        ///crear estructuira de mis datos

        ArrayList<Datos> datos = new ArrayList<Datos>();

        int[] imagenes = {R.drawable.uno1, R.drawable.dos2, R.drawable.tres3, R.drawable.cuatro4, R.drawable.cinco5, R.drawable.seis6, R.drawable.siete7
                , R.drawable.ocho8, R.drawable.nueve9, R.drawable.unoin, R.drawable.dosin, R.drawable.tresin, R.drawable.cuatroin, R.drawable.cincoin, R.drawable.seisin,
                R.drawable.sietein, R.drawable.ochoin, R.drawable.nuevein, R.drawable.uva, R.drawable.oso, R.drawable.dulces, R.drawable.tazas, R.drawable.tambores,
                R.drawable.tacos, R.drawable.conejo, R.drawable.focos, R.drawable.corbatas, R.drawable.fantasmas, R.drawable.sombreros, R.drawable.soles,
                R.drawable.sillas, R.drawable.sandias, R.drawable.estrellas, R.drawable.ojos, R.drawable.naranjas, R.drawable.nubes};

        String[] nombres = {"UNO", "DOS", "TRES", "CUATRO", "CINCO", "SEIS", "SIETE", "OCHO", "NUEVE", "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN",
                "EIGHT", "NINE", "1 UVAS", "1 OSO", "2 DULCES", "2 TAZAS", "3 TAMBORES", "3 TACOS", "4 CONEJOS", "4 FOCOS", "5 CORBATAS", "5 FANTASMAS",
                "6 SOMBREROS", "6 SOLES", "7 SILLAS", "7 SANDIAS", "8 ESTRELLAS", "8 OJOS", "9 NARANJAS", "9 NUBES", "CAMPEON"};
        for (int i = 0; i < 37; i++) {
            //if (sharedPref.getBoolean("trofeo" + i, false))
            if ((contador / 5) < i + 1)
                datos.add(new Datos(i + 1, R.drawable.lock, "???"));
            else
                datos.add(new Datos(i + 1, imagenes[i], nombres[i]));
        }

        Adaptador miadaptador = new Adaptador(getApplicationContext(), datos);
        grilla.setAdapter(miadaptador);

        final int contadorfinal = contador;
        grilla.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                //if (sharedPref.getBoolean("trofeo" + i, false))
                if (((contadorfinal / 5) > position)) {
                    click.play();
                    Ajustes.vibrar(Trofeos.this, 50);
                    Datos obj = (Datos) parent.getItemAtPosition(position);
                    Intent paso = new Intent(getApplicationContext(), Detalle.class);
                    paso.putExtra("objeto", (Serializable) obj);
                    paso.putExtra("id", position);
                    startActivity(paso);

                }

            }

        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            click.play();
            Ajustes.vibrar(this, 50);
            Intent intent = new Intent(Trofeos.this, Principal.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        click.play();
        Ajustes.vibrar(this, 50);
        Intent intent = new Intent(Trofeos.this, Principal.class);
        startActivity(intent);
        finish();
    }


}
