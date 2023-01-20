package com.gametops.memorianumerica;

import java.io.Serializable;

/**
 * Created by User on 29/10/2018.
 */

public class Datos implements Serializable {
    private Integer Id;
    private Integer Imagen;
    private String Titulo;

    public Datos(Integer id, Integer imagen, String titulo) {
        Id = id;
        Imagen = imagen;
        Titulo = titulo;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getImagen() {
        return Imagen;
    }

    public void setImagen(Integer imagen) {
        Imagen = imagen;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }
}
