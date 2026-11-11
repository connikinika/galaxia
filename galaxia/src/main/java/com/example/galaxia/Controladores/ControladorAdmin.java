package com.example.galaxia.Controladores;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.galaxia.Modelo.AlmacenArchivos;
import com.example.galaxia.Modelo.Ajustes;
import com.example.galaxia.Modelo.CatalogoAjustes;
import com.example.galaxia.Modelo.CatalogoCodigos;
import com.example.galaxia.Modelo.Codigo;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class ControladorAdmin {

    private CatalogoCodigos catcodigos;
    private CatalogoAjustes catajustes;
    private AlmacenArchivos almacen;

    @Value("${admin.usuario}")
    private String adminUsuario;
    @Value("${admin.clave}")
    private String adminClave;

    public ControladorAdmin(CatalogoCodigos catcodigos, CatalogoAjustes catajustes, AlmacenArchivos almacen) {
        this.catcodigos = catcodigos;
        this.catajustes = catajustes;
        this.almacen = almacen;
    }

    private boolean esAdmin(HttpSession sesion) {
        return Boolean.TRUE.equals(sesion.getAttribute("admin"));
    }

    @GetMapping("/login")
    public String login_formulario() {
        return "adminLogin";
    }

    @PostMapping("/login")
    public String login(@RequestParam String usuario, @RequestParam String clave, HttpSession sesion, Model model) {
        if (adminUsuario.equals(usuario) && adminClave.equals(clave)) {
            sesion.setAttribute("admin", true);
            return "redirect:/admin";
        }
        model.addAttribute("error", "Usuario o clave incorrectos.");
        return "adminLogin";
    }

    @GetMapping("/logout")
    public String logout(HttpSession sesion) {
        sesion.invalidate();
        return "redirect:/admin/login";
    }

    @GetMapping
    public String panel(HttpSession sesion, Model model) {
        if (!esAdmin(sesion)) return "redirect:/admin/login";
        model.addAttribute("ajustes", catajustes.obtener());
        model.addAttribute("codigos", catcodigos.listarTodos());
        return "adminPanel";
    }

    @GetMapping("/vista-previa")
    public String vista_previa(HttpSession sesion, Model model) {
        if (!esAdmin(sesion)) return "redirect:/admin/login";
        model.addAttribute("ajustes", catajustes.obtener());
        model.addAttribute("vistaPrevia", true);
        return "experiencia";
    }

    @PostMapping("/ajustes")
    public String guardar_ajustes(@RequestParam String titulo, @RequestParam String emoji,
            @RequestParam String frases, @RequestParam String urlBase,
            @RequestParam(required = false) MultipartFile musica,
            @RequestParam(required = false) MultipartFile video,
            HttpSession sesion, RedirectAttributes aviso) throws IOException {
        if (!esAdmin(sesion)) return "redirect:/admin/login";
        Ajustes a = catajustes.obtener();
        a.setTitulo(titulo);
        a.setEmoji(emoji);
        a.setFrases(frases);
        a.setUrlBase(urlBase.isBlank() ? "http://localhost:8080" : urlBase.trim());
        if (musica != null && !musica.isEmpty()) {
            almacen.borrar(a.getMusica());
            a.setMusica(almacen.guardar(musica, "musica"));
        }
        if (video != null && !video.isEmpty()) {
            almacen.borrar(a.getVideo());
            a.setVideo(almacen.guardar(video, "video"));
        }
        aviso.addFlashAttribute("mensaje", "Cambios guardados.");
        return "redirect:/admin";
    }

    @PostMapping("/ajustes/quitar/{tipo}")
    public String quitar(@PathVariable String tipo, HttpSession sesion) {
        if (!esAdmin(sesion)) return "redirect:/admin/login";
        Ajustes a = catajustes.obtener();
        if (tipo.equals("musica")) { almacen.borrar(a.getMusica()); a.setMusica(null); }
        if (tipo.equals("video")) { almacen.borrar(a.getVideo()); a.setVideo(null); }
        return "redirect:/admin";
    }

    @PostMapping("/codigos/generar")
    public String generar(@RequestParam(defaultValue = "") String etiqueta, HttpSession sesion, RedirectAttributes aviso) {
        if (!esAdmin(sesion)) return "redirect:/admin/login";
        Codigo c = catcodigos.generar(etiqueta);
        aviso.addFlashAttribute("mensaje", "Código creado: " + c.getValorFormateado());
        return "redirect:/admin#codigos";
    }

    @PostMapping("/codigos/{id}/eliminar")
    public String eliminar(@PathVariable Long id, HttpSession sesion) {
        if (!esAdmin(sesion)) return "redirect:/admin/login";
        catcodigos.eliminar(id);
        return "redirect:/admin#codigos";
    }

    // El QR lleva a /entrada/{codigo}: escanear = entrar directo
    @GetMapping("/codigos/{id}/qr.png")
    public ResponseEntity<byte[]> qr(@PathVariable Long id, HttpSession sesion) throws Exception {
        if (!esAdmin(sesion)) return ResponseEntity.status(401).build();
        Codigo c = catcodigos.buscarPorId(id).orElse(null);
        if (c == null) return ResponseEntity.notFound().build();
        String enlace = catajustes.obtener().getUrlBaseLimpia() + "/entrada/" + c.getValor();
        BitMatrix matriz = new QRCodeWriter().encode(enlace, BarcodeFormat.QR_CODE, 360, 360,
                Map.of(EncodeHintType.MARGIN, 2));
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matriz, "PNG", salida);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG)
                .header("Cache-Control", "no-store").body(salida.toByteArray());
    }
}
