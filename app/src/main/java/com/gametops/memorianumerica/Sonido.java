package com.gametops.memorianumerica;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;

public class Sonido {

    Integer idSonido;
    Context context;
    MediaPlayer mediaPlay;
    SharedPreferences sharedPref;

    Sonido(Integer idSonido, Context context) {
        this.idSonido = idSonido;
        this.context = context;
        sharedPref = context.getSharedPreferences("record", Context.MODE_PRIVATE);
        mediaPlay = MediaPlayer.create(context, idSonido);
    }


    public void play() {
        if (sharedPref.getBoolean("sonido", true)) {
            mediaPlay.start();
        }
    }


    public void stop() {
        if (sharedPref.getBoolean("sonido", true)) {
            if (mediaPlay.isPlaying())
                mediaPlay.stop();
        }
    }

    public void pause() {
        if (sharedPref.getBoolean("sonido", true)) {
            if (mediaPlay.isPlaying())
                mediaPlay.pause();
        }

    }

}
