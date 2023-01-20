package com.gametops.memorianumerica;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapterNivel extends PagerAdapter {
    Context context;
    LayoutInflater inflater;
    int dificultadarray[];


    int[] botones = {R.drawable.ju1, R.drawable.ju2, R.drawable.ju3, R.drawable.ju1};

    Typeface font;
    int[] record = new int[27];
    boolean[] contestadas = new boolean[30];
    int dificultad;
    SharedPreferences sharedPref;


    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.viewpagernivel, container, false);
        LinearLayout lin = view.findViewById(R.id.linealayoutmenu);
        sharedPref = context.getSharedPreferences("record", Context.MODE_PRIVATE);
        for (int i = 0; i < 27; i++) {
            record[i] = sharedPref.getInt(dificultad + "record" + i, 0);
        }
        contestadas[0] = true;
        for (int i = 0; i < 27; i++) {

            contestadas[i] = sharedPref.getBoolean(dificultad + "contestada" + i, false);
        }
        contestadas[0] = true;


        crearaccesos(3, 3, lin, 9 * position, dificultad);


        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }

    public SliderAdapterNivel(Context context, int dificultadarray[], int dificultad) {
        this.dificultadarray = dificultadarray;
        this.context = context;
        this.dificultad = dificultad;
    }


    public void crearaccesos(int mancho, int mlargo, LinearLayout layout, int position, int id) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        font = Typeface.createFromAsset(context.getAssets(), "fonts/birdyame.ttf");

        for (int i = 0; i <= (mlargo); i++) {
            LinearLayout row = new LinearLayout(context);
            row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            LinearLayout.LayoutParams lll = (LinearLayout.LayoutParams) row.getLayoutParams();
            lll.gravity = Gravity.CENTER;
            row.setLayoutParams(lll);

            for (int j = 0; j < mancho; j++) {

                if ((j + (i * mancho)) <= (mancho * mlargo) - 1) {
                    View inflar = View.inflate(context, R.layout.layout_memorama, null);
                    ImageView boton = inflar.findViewById(R.id.botonmemorama);
                    TextView textView = inflar.findViewById(R.id.numero);
                    boton.getLayoutParams().height = metrics.widthPixels / 4;
                    boton.getLayoutParams().width = metrics.widthPixels / 4;
                    boton.setBackground(context.getResources().getDrawable(botones[id]));

                    if (!contestadas[position + j + (i * mancho)]) {
                        boton.setEnabled(false);
                        boton.setBackground(context.getResources().getDrawable(R.drawable.lock));
                    } else {
                        textView.setTypeface(font);
                        //boton.setText("" + position);
                        textView.setText("" + (1 + position + j + (i * mancho)));
                    }

                    ImageView estrella1 = inflar.findViewById(R.id.star01);
                    ImageView estrella2 = inflar.findViewById(R.id.star02);
                    ImageView estrella3 = inflar.findViewById(R.id.star03);
                    switch (record[position + j + (i * mancho)]) {
                        case 1:
                            estrella1.setBackground(context.getResources().getDrawable(R.drawable.star));
                            break;
                        case 2:
                            estrella1.setBackground(context.getResources().getDrawable(R.drawable.star));
                            estrella2.setBackground(context.getResources().getDrawable(R.drawable.star));
                            break;
                        case 3:
                            estrella1.setBackground(context.getResources().getDrawable(R.drawable.star));
                            estrella2.setBackground(context.getResources().getDrawable(R.drawable.star));
                            estrella3.setBackground(context.getResources().getDrawable(R.drawable.star));
                            break;
                    }
                    final int num = position + j + (i * mancho);

                    boton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Ajustes.vibrar(context, 50);
                            Sonido click = new Sonido(R.raw.click, context);
                            click.play();

                            Intent intent = new Intent(context, Memorama.class);
                            intent.putExtra("dificultad", dificultad);
                            intent.putExtra("id", num);
                            context.startActivity(intent);
                        }
                    });

                    row.addView(inflar);
                    row.setGravity(Gravity.CENTER);
                }
            }
            layout.addView(row);
        }
        layout.setGravity(Gravity.CENTER);

    }

}
