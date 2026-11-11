package com.example.galaxia.Modelo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CatalogoAjustes {

    private final Ajustes ajustes = new Ajustes();

    // La URL base de los QR arranca con el valor de app.url-base
    public CatalogoAjustes(@Value("${app.url-base:http://localhost:8080}")) String urlBase) {
        ajustes.setUrlBase(urlBase);
    }

    public Ajustes obtener() {
        return ajustes;
    }
}
