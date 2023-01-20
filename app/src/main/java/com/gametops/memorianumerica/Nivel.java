package com.gametops.memorianumerica;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


public class Nivel extends AppCompatActivity implements View.OnClickListener {
    private ViewPager viewpager;
    private SliderAdapterNivel adapter;
    private static final float MIN_SCALE = 0.7f;
    private static final float MIN_ALPHA = 0.3f;
    int pagina = 0;

    SharedPreferences sharedPref;
    private AdView mAdView;
    Sonido click;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nivel);
        viewpager = findViewById(R.id.viewpager);
        TextView txtest = findViewById(R.id.txtestrella);
        click = new Sonido(R.raw.click, this);
        sharedPref = getSharedPreferences("record", Context.MODE_PRIVATE);
        int califica = sharedPref.getInt("califica", 0);
        if (califica == 30) {
            dialogocalifica();
            califica = 0;
        } else
            califica++;

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("califica", califica);
        editor.apply();

       // MobileAds.initialize(this, "ca-app-pub-1984616735532779~3068362417");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        int contador = 0;

        for (int n = 0; n < 4; n++) {
            for (int i = 0; i < 27; i++) {
                contador += sharedPref.getInt(n + "record" + i, 0);
            }
        }
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/birdyame.ttf");
        txtest.setText(contador + " X ");
        txtest.setTypeface(font);

        adapter = new SliderAdapterNivel(this, new int[]{0, 1, 2, 3, 4, 5,}, getIntent().getExtras().getInt("dificultad"));
        viewpager.setAdapter(adapter);
        pagina = getIntent().getIntExtra("pagina", 0);
        viewpager.setCurrentItem(pagina);

        //aqui se pone la animacion de transicion
        viewpager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(@NonNull View view, float position) {
                int pageWidth = view.getWidth();
                int pageHeight = view.getHeight();

                if (position < -1) { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    view.setAlpha(0f);

                } else if (position <= 1) { // [-1,1]
                    // Modify the default slide transition to shrink the page as well
                    float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                    float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                    float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                    if (position < 0) {
                        view.setTranslationX(horzMargin - vertMargin / 2);
                    } else {
                        view.setTranslationX(-horzMargin + vertMargin / 2);

                    }

                    // Scale the page down (between MIN_SCALE and 1)
                    view.setScaleX(scaleFactor);
                    view.setScaleY(scaleFactor);

                    // Fade the page relative to its size.
                    view.setAlpha(MIN_ALPHA +
                            (scaleFactor - MIN_SCALE) /
                                    (1 - MIN_SCALE) * (1 - MIN_ALPHA));

                } else { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    view.setAlpha(0f);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        Ajustes.vibrar(this, 50);
        click.play();
        Intent intent = new Intent(Nivel.this, Niveles.class);
        intent.putExtra("dificultad", adapter.dificultad);
        startActivity(intent);
        finish();

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Ajustes.vibrar(this, 50);
            click.play();
            Intent intent = new Intent(Nivel.this, Niveles.class);
            intent.putExtra("dificultad", adapter.dificultad);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void dialogocalifica() {
        ColorDrawable dialogColor = new ColorDrawable(Color.GRAY);
        dialogColor.setAlpha(0);
        final AlertDialog.Builder builder = new AlertDialog.Builder(Nivel.this);
        final LayoutInflater inflater = getLayoutInflater();
        View vi = inflater.inflate(R.layout.dialogocalifica, null);
        builder.setView(vi);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(dialogColor);
        Button botonsi = vi.findViewById(R.id.botonsi);
        botonsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ajustes.vibrar(Nivel.this, 50);
                click.play();
                Intent intentae4 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.toposdeus.memorama"));
                startActivity(intentae4);
            }
        });
        Button botonno = vi.findViewById(R.id.botonno);
        botonno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ajustes.vibrar(Nivel.this, 50);
                click.play();
                dialog.cancel();
            }
        });

        dialog.show();

    }
}
