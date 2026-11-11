package com.example.galaxia.Modelo;

import java.util.Arrays;
import java.util.List;

// Lo que el admin edita: textos, musica y video de fondo
public class Ajustes {

    private String titulo = "Para ti, con cariño";
    private String emoji = "\uD83C\uDF3B";
    private String frases = "Eres luz en mi universo\nGracias por existir\nHoy brillas más que cualquier estrella";
    private String musica; // nombre del archivo en /uploads (null = sin musica)
    private String video;  // igual que musica
    private String urlBase = "http://localhost:8080";

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getEmoji() { return emoji; }
    public void setEmoji(String emoji) { this.emoji = emoji; }
    public String getFrases() { return frases; }
    public void setFrases(String frases) { this.frases = frases; }
    public String getMusica() { return musica; }
    public void setMusica(String musica) { this.musica = musica; }
    public String getVideo() { return video; }
    public void setVideo(String video) { this.video = video; }
    public String getUrlBase() { return urlBase; }
    public void setUrlBase(String urlBase) { this.urlBase = urlBase; }

    public String getUrlBaseLimpia() { return urlBase.replaceAll("/+$", ""); }

    public List<String> getListaFrases() {
        return Arrays.stream(frases.split("\\R")).map(String::trim).filter(f -> !f.isEmpty()).toList();
    }
}
