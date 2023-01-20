package com.gametops.memorianumerica;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;




public class Niveles extends AppCompatActivity implements View.OnClickListener {
    private SliderAdapterNiveles adapter;
    private static final float MIN_SCALE = 0.7f;
    private static final float MIN_ALPHA = 0.3f;
    int pagina;
    SharedPreferences sharedPref;
    private AdView mAdView;
    Sonido click;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_niveles);
        ViewPager viewpager = findViewById(R.id.viewpager);
        adapter = new SliderAdapterNiveles(this, new int[]{0, 1, 2, 3, 4, 5,}, this);
        viewpager.setAdapter(adapter);

        sharedPref = getSharedPreferences("record", Context.MODE_PRIVATE);
      //  MobileAds.initialize(this, "ca-app-pub-1984616735532779~3068362417");

        click = new Sonido(R.raw.click, this);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        if (sharedPref.getBoolean("instrucciones", true)) {
            dialogo();
        }

        TextView txtest = findViewById(R.id.txtestrella);
        int contador = 0;
        for (int n = 0; n < 3; n++) {
            for (int i = 0; i < 27; i++) {
                contador += sharedPref.getInt(n + "record" + i , 0);
            }
        }
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/birdyame.ttf");
        txtest.setText(contador + " X ");
        txtest.setTypeface(font);

        pagina = getIntent().getIntExtra("dificultad", 0);
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
        click.play();
        Ajustes.vibrar(this, 50);
        Intent intent = new Intent(Niveles.this, Principal.class);
        intent.putExtra("dificultad", adapter.dificultad);
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        click.play();
        Ajustes.vibrar(this, 50);
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(Niveles.this, Principal.class);
            intent.putExtra("dificultad", adapter.dificultad);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void dialogo() {
        Animation flechaanim = AnimationUtils.loadAnimation(Niveles.this, R.anim.moverflecha);
        ColorDrawable dialogColor = new ColorDrawable(Color.GRAY);
        dialogColor.setAlpha(0);
        final AlertDialog.Builder builder = new AlertDialog.Builder(Niveles.this);
        LayoutInflater inflater = getLayoutInflater();
        View vi = inflater.inflate(R.layout.instrucciones, null);
        final CheckBox checkbox = vi.findViewById(R.id.checkbox);
        ImageView flecha = vi.findViewById(R.id.flecha1);
        LinearLayout lay = vi.findViewById(R.id.layoutinstrucciones);
        flecha.startAnimation(flechaanim);
        builder.setView(vi);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.play();
                Ajustes.vibrar(Niveles.this, 50);
                if (checkbox.isChecked()) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("instrucciones", false);
                    editor.commit();
                }
                dialog.cancel();

            }
        });
        dialog.getWindow().setBackgroundDrawable(dialogColor);
        dialog.show();

    }


}