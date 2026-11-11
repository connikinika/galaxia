package com.example.galaxia.Controladores;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.galaxia.Modelo.CatalogoAjustes;
import com.example.galaxia.Modelo.CatalogoCodigos;
import com.example.galaxia.Modelo.Codigo;

import jakarta.servlet.http.HttpSession;

@Controller
public class ControladorEntrada {

    private CatalogoCodigos catcodigos;
    private CatalogoAjustes catajustes;

    public ControladorEntrada(CatalogoCodigos catcodigos, CatalogoAjustes catajustes) {
        this.catcodigos = catcodigos;
        this.catajustes = catajustes;
    }

    @GetMapping("/")
    public String inicio() {
        return "redirect:/entrada";
    }

    @GetMapping("/entrada")
    public String formulario(Model model) {
        model.addAttribute("ajustes", catajustes.obtener());
        return "entrada";
    }

    @PostMapping("/entrada")
    public String enviar_codigo(@RequestParam String codigo, HttpSession sesion, Model model) {
        return entrar(codigo, sesion, model);
    }

    // Destino del QR
    @GetMapping("/entrada/{codigo}")
    public String entrada_directa(@PathVariable String codigo, HttpSession sesion, Model model) {
        return entrar(codigo, sesion, model);
    }

    private String entrar(String texto, HttpSession sesion, Model model) {
        Optional<Codigo> codigo = catcodigos.buscarPorValor(texto);
        if (codigo.isEmpty()) {
            model.addAttribute("ajustes", catajustes.obtener());
            model.addAttribute("error", "Ese código no es válido. Revisa que esté completo.");
            return "entrada";
        }
        codigo.get().registrarUso();
        sesion.setAttribute("invitado", codigo.get().getValor());
        return "redirect:/experiencia";
    }

    @GetMapping("/experiencia")
    public String experiencia(HttpSession sesion, Model model) {
        String valor = (String) sesion.getAttribute("invitado");
        if (valor == null || catcodigos.buscarPorValor(valor).isEmpty()) return "redirect:/entrada";
        model.addAttribute("ajustes", catajustes.obtener());
        return "experiencia";
    }
}
