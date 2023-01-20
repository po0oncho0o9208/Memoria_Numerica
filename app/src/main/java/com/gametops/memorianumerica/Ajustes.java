package com.gametops.memorianumerica;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Ajustes extends AppCompatActivity implements View.OnClickListener {

    CheckBox sonido, vibrar;
    Button  reestablecer, botoncomparte, botoncalifica, botoncreditos;
    SharedPreferences sharedPref;
    TextView txtest;
    private AdView mAdView;
    Sonido click;
    ImageView  btnatras;

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones);
        //MobileAds.initialize(this, "ca-app-pub-1984616735532779~3068362417");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        click = new Sonido(R.raw.click, this);

        btnatras = findViewById(R.id.atras);
        btnatras.setOnClickListener(this);
        reestablecer = findViewById(R.id.botonreestablecer);
        reestablecer.setOnClickListener(this);
        txtest = findViewById(R.id.txtestrella);
        botoncreditos = findViewById(R.id.botoncreditos);
        botoncreditos.setOnClickListener(this);
        botoncomparte = findViewById(R.id.botoncomparte);
        botoncomparte.setOnClickListener(this);
        botoncalifica = findViewById(R.id.botoncalifica);
        botoncalifica.setOnClickListener(this);
        sonido = findViewById(R.id.checkboxsonido);
        vibrar = findViewById(R.id.checkboxvibrar);
        sonido.setChecked(Cargarboolean(this, "sonido"));
        vibrar.setChecked(Cargarboolean(this, "vibrar"));
        sonido.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vibrar(Ajustes.this, 50);
                if (isChecked) {
                    Ajustes.Guardarboolean(Ajustes.this, true, "sonido");
                    click.play();

                } else {
                    Ajustes.Guardarboolean(Ajustes.this, false, "sonido");
                }
            }
        });


        vibrar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                click.play();
                if (isChecked) {
                    Ajustes.Guardarboolean(Ajustes.this, true, "vibrar");
                    vibrar(Ajustes.this, 50);

                } else {
                    Ajustes.Guardarboolean(Ajustes.this, false, "vibrar");

                }

            }
        });
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

    }


    @Override
    public void onClick(View v) {
        click.play();
        vibrar(this, 50);
        switch (v.getId()) {
            case R.id.botoncomparte:
                if (ContextCompat.checkSelfPermission(Ajustes.this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(Ajustes.this,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    } else {

                        ActivityCompat.requestPermissions(Ajustes.this,
                                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                    }
                }
                Intent intento = new Intent(Intent.ACTION_SEND);
                intento.setType("*/*");
                String paramString1 = Integer.toString(R.drawable.atras);
                Bitmap topo2 = BitmapFactory.decodeResource(getResources(), R.drawable.atras);
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
                        intento.putExtra(Intent.EXTRA_TEXT, "Descubre que tan buena memoria tienes y ejercitala con esta aplicacion "
                                + Html.fromHtml("<br />") + "https://play.google.com/store/apps/details?id=com.toposdeus.memorama");
                        intento.putExtra(
                                Intent.EXTRA_STREAM,
                                bmpUri);
                        startActivity(Intent.createChooser(intento,
                                "Siguenos en nuestra pagina "));
                    }

                }
                break;
            case R.id.botoncalifica:
                Intent intentae4 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.toposdeus.memorama"));
                startActivity(intentae4);
                break;
            case R.id.botonreestablecer:

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final LayoutInflater inflater = getLayoutInflater();
                View vi = inflater.inflate(R.layout.dialogoconfirm2, null);
                builder.setView(vi);
                final AlertDialog dialog = builder.create();
                //decidir despues si sera cancelable o no
                dialog.setCancelable(false);
                Button botonsi = vi.findViewById(R.id.botonsi1);

                botonsi.setOnClickListener(

                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                vibrar(Ajustes.this, 50);

                                click.play();
                                //sharedPref = getSharedPreferences("record", 0);
                                //sharedPref.edit().remove("record").commit();
                                sharedPref.edit().clear().commit();
                                txtest.setText(0 + " X ");
                                Toast.makeText(Ajustes.this, "Se han restablecido sus datos ", Toast.LENGTH_SHORT).show();
                                dialog.cancel();

                            }
                        }
                );
                Button botonno = vi.findViewById(R.id.botonno1);
                botonno.setOnClickListener(

                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                vibrar(Ajustes.this, 50);
                                click.play();
                                dialog.cancel();

                            }
                        }
                );
                dialog.show();

                break;

            case R.id.atras:
                Intent intent = new Intent(Ajustes.this, Principal.class);
                startActivity(intent);
                finish();
                break;
            case R.id.botoncreditos:
                ColorDrawable dialogColor = new ColorDrawable(Color.GRAY);
                dialogColor.setAlpha(0);
                final AlertDialog.Builder builderd = new AlertDialog.Builder(Ajustes.this);
                final LayoutInflater inflaterd = getLayoutInflater();
                View vid = inflaterd.inflate(R.layout.dialogocalifica, null);
                builderd.setView(vid);
                final AlertDialog dialogd = builderd.create();
                dialogd.setCancelable(true);
                dialogd.getWindow().setBackgroundDrawable(dialogColor);
                Button botonsid = vid.findViewById(R.id.botonsi);
                botonsid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogd.cancel();
                    }
                });

                dialogd.show();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        vibrar(Ajustes.this, 50);
        click.play();
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(Ajustes.this, Principal.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public static boolean Cargarboolean(Context context, String nombre) {
        SharedPreferences sharedPref;
        boolean activado;
        sharedPref = context.getSharedPreferences("record", Context.MODE_PRIVATE);
        activado = sharedPref.getBoolean(nombre, true);
        return activado;

    }

    public static void Guardarboolean(Context contexto, boolean activado, String nombre) {
        SharedPreferences sharedPref;
        sharedPref = contexto.getSharedPreferences(
                "record", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(nombre, activado);
        editor.commit();
    }

    public static void vibrar(Context contexto, int tim) {

        if (Ajustes.Cargarboolean(contexto, "vibrar")) {
            Vibrator vib = (Vibrator) contexto.getSystemService(VIBRATOR_SERVICE);
            vib.vibrate(tim);
        }
    }


}
