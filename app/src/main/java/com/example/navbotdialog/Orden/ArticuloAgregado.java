package com.example.navbotdialog.Orden;

public class ArticuloAgregado {
    private String cantidad;
    private String articulo;

    public ArticuloAgregado(String cantidad, String articulo) {
        this.cantidad = cantidad;
        this.articulo = articulo;
    }

    public String getCantidad() {
        return cantidad;
    }

    public String getArticulo() {
        return articulo;
    }
}
