package com.gametops.memorianumerica;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Detalle extends AppCompatActivity {
    ImageView imagenview;
    int movil;
    MediaPlayer mediaplayer;
    int id;
    SharedPreferences sharedPref;
    private AdView mAdView;
    Sonido click;
    int[] sonidos = {R.raw.uno, R.raw.dos, R.raw.tres, R.raw.cuatro, R.raw.cinco, R.raw.seis, R.raw.siete,
            R.raw.oco, R.raw.nueve1, R.raw.one, R.raw.two, R.raw.tree, R.raw.four, R.raw.five, R.raw.six,
            R.raw.seven, R.raw.eigt, R.raw.nine, R.raw.unauva, R.raw.unoso, R.raw.dosdulces, R.raw.dostazas, R.raw.trestambores, R.raw.trestacos,
            R.raw.cuatroconejos, R.raw.cuatrofocos, R.raw.cincocorbatas, R.raw.cincofantasmas, R.raw.seissombreros, R.raw.seissoles, R.raw.sietesillas, R.raw.sietesandias,
            R.raw.ocoestrellas, R.raw.ocoojos, R.raw.nuevenarajas, R.raw.nuevenubes, R.raw.felicidades2, R.raw.dugget, R.raw.riptide, R.raw.winner};

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
       // MobileAds.initialize(this, "ca-app-pub-1984616735532779~3068362417");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        int contador = 0;
        ImageView back = findViewById(R.id.atras);
        click = new Sonido(R.raw.click, this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ajustes.vibrar(Detalle.this, 50);
                click.play();
                Intent intent = new Intent(Detalle.this, Trofeos.class);
                startActivity(intent);
                finish();
            }
        });

        TextView txtest = findViewById(R.id.txtestrella);
        sharedPref = getSharedPreferences("record", Context.MODE_PRIVATE);

        for (int n = 0; n < 4; n++) {
            for (int i = 0; i < 27; i++) {
                contador += sharedPref.getInt(n + "record" + i, 0);
            }
        }
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/birdyame.ttf");
        txtest.setText(contador + " X ");
        txtest.setTypeface(font);

        imagenview = findViewById(R.id.iamgendetalle);

        Datos obj = (Datos) getIntent().getExtras().getSerializable("objeto");
        id = getIntent().getExtras().getInt("id");
        imagenview.setImageResource(obj.getImagen());
        movil = obj.getImagen();


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(Detalle.this, Trofeos.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void Compartir1(View view) {

        if (ContextCompat.checkSelfPermission(Detalle.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Detalle.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {

                ActivityCompat.requestPermissions(Detalle.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }

        Intent intento = new Intent(Intent.ACTION_SEND);
        intento.setType("*/*");
        String paramString1 = Integer.toString(movil);
        Bitmap topo2 = BitmapFactory.decodeStream(getResources().openRawResource(movil));
        String fileName = paramString1 + "" + ".png";
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        topo2.compress(Bitmap.CompressFormat.PNG, 40, bytes);
        File ExternalStorageDirectory = Environment.getExternalStorageDirectory();
        File file = new File(ExternalStorageDirectory + File.separator + fileName);
        FileOutputStream fileOutputStream = null;
        try {
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes.toByteArray());
        } catch (IOException e) {

        } finally {
            if (fileOutputStream != null) {
                Uri bmpUri = Uri.parse(file.getPath());
                intento.putExtra(Intent.EXTRA_TEXT, "Comparte tú pasión futbolera con tú persona favorita" + Html.fromHtml("<br />") +
                        "solo descargar nuestra aplicacion y llevate los mejores protectores de pantalla para tu celular " + Html.fromHtml("<br />") +
                        "https://play.google.com/store/apps/details?id=com.games.user.americahd");
                intento.putExtra(
                        Intent.EXTRA_STREAM,
                        bmpUri);
                startActivity(Intent.createChooser(intento,
                        "Siguenos en nuestra pagina "));
            }
        }
    }

    public void Seguir(View view) {
        Intent intenta7 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Desarrollador-Topos-Deus-423579291439302/"));
        startActivity(intenta7);


    }


    public void playsound(View view) {
        Sonido sonido = new Sonido(sonidos[id], this);
        sonido.play();

    }
}


