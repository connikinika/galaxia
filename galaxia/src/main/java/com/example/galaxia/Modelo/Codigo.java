package com.example.galaxia.Modelo;

public class Codigo {

    private Long id;
    private String valor;    // 8 caracteres, sin guion
    private String etiqueta; // para quien es
    private int usos;

    public Codigo(Long id, String valor, String etiqueta) {
        this.id = id;
        this.valor = valor;
        this.etiqueta = etiqueta;
    }

    public Long getId() { return id; }
    public String getValor() { return valor; }
    public String getEtiqueta() { return etiqueta; }
    public int getUsos() { return usos; }
    public void registrarUso() { usos++; }

    public String getValorFormateado() { return valor.substring(0, 4) + "-" + valor.substring(4); }
}
