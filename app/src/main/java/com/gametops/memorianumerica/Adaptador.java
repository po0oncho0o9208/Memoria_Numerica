package com.gametops.memorianumerica;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by User on 29/10/2018.
 */

public class Adaptador extends BaseAdapter {
    private Context contexto;
    private List<Datos> lista;

    public Adaptador(Context contexto, List<Datos> lista) {
        this.contexto = contexto;
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return lista.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflate = LayoutInflater.from((contexto));
        convertView = inflate.inflate(R.layout.griditem, null);
        ImageView imagen = convertView.findViewById(R.id.imgimagen);
        Typeface font = Typeface.createFromAsset(contexto.getAssets(), "fonts/birdyame.ttf");
        TextView titulo = convertView.findViewById(R.id.txttitulo);
        titulo.setTypeface(font);
        titulo.setText(lista.get(position).getTitulo().toString());
        imagen.setImageResource(lista.get(position).getImagen());
        return convertView;

    }

}

