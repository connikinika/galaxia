package com.example.galaxia.Modelo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

// Guarda la musica y el video en la carpeta ./uploads
@Component
public class AlmacenArchivos {

    private final Path carpeta = Paths.get("uploads");

    public AlmacenArchivos() throws IOException {
        Files.createDirectories(carpeta);
    }

    public String guardar(MultipartFile archivo, String prefijo) throws IOException {
        String original = archivo.getOriginalFilename() == null ? "" : archivo.getOriginalFilename();
        int punto = original.lastIndexOf('.');
        String ext = punto >= 0 ? original.substring(punto).toLowerCase().replaceAll("[^.a-z0-9]", "") : "";
        String nombre = prefijo + "-" + UUID.randomUUID() + ext;
        Files.copy(archivo.getInputStream(), carpeta.resolve(nombre));
        return nombre;
    }

    public void borrar(String nombre) {
        if (nombre == null) return;
        try {
            Files.deleteIfExists(carpeta.resolve(nombre));
        } catch (IOException e) {
            // si no se pudo borrar no pasa nada
        }
    }
}
