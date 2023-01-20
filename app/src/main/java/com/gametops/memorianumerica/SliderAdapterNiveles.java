package com.gametops.memorianumerica;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapterNiveles extends PagerAdapter {
    Context context;
    LayoutInflater inflater;
    int dificultad[];
    Activity activity;
    public boolean sfin = false;
    public int[] lista = {R.drawable.btnjugar1, R.drawable.btnjugar2, R.drawable.btnjugar3, R.drawable.btnjugar4};
    public int[] titulosimagen = {R.drawable.titulonormal, R.drawable.titulopreciso, R.drawable.titulocontrareloj, R.drawable.titulolibre};
    public String[] titulos = new String[]{"", "", "", ""};
    boolean[] contestadas = new boolean[27];
    int[] paginas = {R.layout.viewpager, R.layout.viewpager, R.layout.viewpager, R.layout.dialogolibre};
    SharedPreferences sharedPref;
    String dimensiones;


    @Override
    public int getCount() {
        return lista.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        sharedPref = context.getSharedPreferences("record", Context.MODE_PRIVATE);

        View view = inflater.inflate(paginas[position], container, false);
        ImageView boton = view.findViewById(R.id.botonjugar);
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/birdyame.ttf");
        TextView titulo = view.findViewById(R.id.slidertitulo);
        titulo.setTypeface(font);
        titulo.setText(titulos[position]);
        titulo.setBackground(context.getResources().getDrawable(titulosimagen[position]));
        int contador = 0;
        int contadorestrellas = 0;
        TextView txvwresueltas = view.findViewById(R.id.resueltas);
        TextView txtestrellas = view.findViewById(R.id.estrellas);

        for (int i = 0; i < 27; i++) {
            if (contestadas[i] = sharedPref.getBoolean(position + "contestada" + i, false)) {
                contador++;
            }
        }
        for (int i = 0; i < 27; i++) {
            contadorestrellas += sharedPref.getInt(position + "record" + i, 0);
        }

        int contadores = 0;
        for (int n = 0; n < 3; n++) {
            for (int i = 0; i < 27; i++) {
                contadores += sharedPref.getInt(n + "record" + i, 0);
            }
        }


        switch (position) {

            case 0:
                TextView falt = view.findViewById(R.id.txtfaltantes);
                falt.setVisibility(View.GONE);
                txvwresueltas.setTypeface(font);
                txtestrellas.setTypeface(font);
                txvwresueltas.setTextSize(45);
                txtestrellas.setTextSize(35);
                txvwresueltas.setTextColor(context.getResources().getColor(R.color.white));
                txtestrellas.setTextColor(context.getResources().getColor(R.color.white));
                txtestrellas.setText(contadorestrellas + " X ");
                txvwresueltas.setText("" + contador + "/27");
                break;
            case 1:
                TextView falt1 = view.findViewById(R.id.txtfaltantes);
                if (contadores < 27) {
                    falt1.setText("Desbloquea con 27 estrellas ");
                    LinearLayout lay = view.findViewById(R.id.layoutspinners);
                    lay.setVisibility(View.INVISIBLE);
                    boton.setEnabled(false);
                } else {
                    txvwresueltas.setTypeface(font);
                    txtestrellas.setTypeface(font);
                    txvwresueltas.setTextSize(45);
                    txtestrellas.setTextSize(35);
                    txvwresueltas.setTextColor(context.getResources().getColor(R.color.white));
                    txtestrellas.setTextColor(context.getResources().getColor(R.color.white));
                    txtestrellas.setText(contadorestrellas + " X ");
                    txvwresueltas.setText("" + contador + "/27");
                    falt1.setVisibility(View.GONE);
                }

                break;
            case 2:
                TextView falt2 = view.findViewById(R.id.txtfaltantes);

                if (contadores < 54) {
                    falt2.setText("Desbloquea con 54 estrellas ");
                    LinearLayout lay = view.findViewById(R.id.layoutspinners);
                    lay.setVisibility(View.INVISIBLE);
                    boton.setEnabled(false);
                } else {
                    txvwresueltas.setTypeface(font);
                    txtestrellas.setTypeface(font);
                    txvwresueltas.setTextSize(45);
                    txtestrellas.setTextSize(35);
                    txvwresueltas.setTextColor(context.getResources().getColor(R.color.white));
                    txtestrellas.setTextColor(context.getResources().getColor(R.color.white));
                    txtestrellas.setText(contadorestrellas + " X ");
                    txvwresueltas.setText("" + contador + "/27");
                    falt2.setVisibility(View.GONE);
                }

                break;
            case 3:

                Spinner spinnerl = view.findViewById(R.id.spinnerl);
                ArrayAdapter<CharSequence> adaptadorl = ArrayAdapter.createFromResource(context, R.array.ancho, R.layout.spinner_itemview);
                spinnerl.setAdapter(adaptadorl);
                spinnerl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        dimensiones = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                TextView falt3 = view.findViewById(R.id.txtfaltantes);
                if (contadores < 81) {
                    falt3.setText("Desbloquea con 81 estrellas ");
                    LinearLayout lay = view.findViewById(R.id.layoutspinners);
                    lay.setVisibility(View.INVISIBLE);
                    boton.setEnabled(false);
                } else {
                    falt3.setVisibility(View.GONE);
                }
                break;
        }


        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sonido click = new Sonido(R.raw.click, context);
                click.play();
                Ajustes.vibrar(context, 50);
                sfin = true;
                if (position == 3) {
                    Intent intent = new Intent(context, Memorama.class);
                    intent.putExtra("dificultad", position);
                    intent.putExtra("dimensiones", dimensiones);
                    intent.putExtra("id", 0);
                    context.startActivity(intent);
                    activity.finish();
                } else {
                    Intent intent = new Intent(context, Nivel.class);
                    intent.putExtra("dificultad", position);
                    context.startActivity(intent);
                    activity.finish();
                }
            }
        });

        boton.setBackgroundResource(lista[position]);
        //boton.setBackground(context.getResources().getDrawable(lista[position]));

        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }

    public SliderAdapterNiveles(Context context, int dificultad[], Activity activity) {
        this.dificultad = dificultad;
        this.context = context;
        this.activity = activity;
    }

}
