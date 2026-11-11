package com.example.galaxia.Modelo;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

@Component // Spring crea UN objeto de esta clase y lo reparte a quien lo pida
public class CatalogoCodigos {

    // Sin 0/O/1/I para que no se confundan al escribirlos
    private static final String ALFABETO = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

    private final List<Codigo> codigos = new CopyOnWriteArrayList<>();
    private final AtomicLong secuencia = new AtomicLong();
    private final SecureRandom azar = new SecureRandom();

    public List<Codigo> listarTodos() {
        return codigos;
    }

    public Codigo generar(String etiqueta) {
        String valor;
        do {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 8; i++) sb.append(ALFABETO.charAt(azar.nextInt(ALFABETO.length())));
            valor = sb.toString();
        } while (buscarPorValor(valor).isPresent());
        Codigo c = new Codigo(secuencia.incrementAndGet(), valor, etiqueta == null ? "" : etiqueta.trim());
        codigos.add(c);
        return c;
    }

    public Optional<Codigo> buscarPorId(Long id) {
        return codigos.stream().filter(c -> c.getId().equals(id)).findFirst();
    }

    // Acepta minusculas, espacios y guiones: "abcd-1234" == "ABCD1234"
    public Optional<Codigo> buscarPorValor(String texto) {
        String limpio = texto == null ? "" : texto.toUpperCase().replaceAll("[^A-Z0-9]", "");
        return codigos.stream().filter(c -> c.getValor().equals(limpio)).findFirst();
    }

    public void eliminar(Long id) {
        buscarPorId(id).ifPresent(codigos::remove);
    }
}
