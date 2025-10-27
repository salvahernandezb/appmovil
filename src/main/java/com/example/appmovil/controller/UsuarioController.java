package com.example.appmovil.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.appmovil.model.Cupo;
import com.example.appmovil.model.Usuario;
import com.example.appmovil.service.CupoService;
import com.example.appmovil.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    
    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CupoService cupoService;

    @GetMapping("/{nombre}/cupos/{movil}")
    public ResponseEntity<Cupo> getCupo(@PathVariable String nombre, @PathVariable String movil) {
        try {
            Usuario usuario = usuarioService.findByNombre(nombre);
            if (usuario == null) {
                return ResponseEntity.notFound().build();
            }
            Cupo cupo = cupoService.getCupoByUsuarioAndMovil(usuario, movil);
            if (cupo == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(cupo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{nombre}/cupos")
    public ResponseEntity<Cupo> createCupo(
            @PathVariable String nombre,
            @RequestParam String movil,
            @RequestParam Double saldo,
            @RequestParam Double datos,
            @RequestParam String plataforma) {
        
        try {
            // Buscar o crear el usuario
            Usuario usuario = usuarioService.findOrCreateByNombre(nombre);

            // Crear cupo
            Cupo cupo = cupoService.createCupo(nombre, movil, saldo, datos, plataforma);
            return ResponseEntity.status(HttpStatus.CREATED).body(cupo);
        } catch (Exception e) {
            logger.error("Error creating cupo: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
