package com.example.navbotdialog.Scanner;

public class OrderArticle {
    private int id_orden;
    private int id_articulo;

    public OrderArticle(int idOrden, int idArticulo) {
        this.id_orden = idOrden;
        this.id_articulo = idArticulo;
    }

    public int getId_orden() {
        return id_orden;
    }

    public int getId_articulo() {
        return id_articulo;
    }
}

