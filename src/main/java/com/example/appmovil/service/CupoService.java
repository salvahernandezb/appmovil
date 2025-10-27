package com.example.appmovil.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.appmovil.model.Cupo;
import com.example.appmovil.model.Usuario;
import com.example.appmovil.repository.CupoRepository;

@Service
public class CupoService {
    
    private static final Logger logger = LoggerFactory.getLogger(CupoService.class);

    @Autowired
    private CupoRepository cupoRepository;

    @Autowired
    private UsuarioService usuarioService;

    public Cupo createCupo(String nombreUsuario, String movil, Double saldo, Double datos, String plataforma) {
        try {
            Usuario usuario = usuarioService.findOrCreateByNombre(nombreUsuario);
            logger.info("Usuario encontrado/creado: {}", nombreUsuario);
            
            Cupo cupo = new Cupo(movil, saldo, datos, plataforma);
            cupo.setUsuario(usuario);
            
            Cupo savedCupo = cupoRepository.save(cupo);
            logger.info("Cupo creado exitosamente para el usuario: {} y móvil: {}", nombreUsuario, movil);
            return savedCupo;
        } catch (Exception e) {
            logger.error("Error al crear cupo para usuario: " + nombreUsuario, e);
            throw e;
        }
    }

    public Cupo getCupoByUsuarioAndMovil(Usuario usuario, String movil) {
        return cupoRepository.findByUsuarioAndMovil(usuario, movil).orElse(null);
    }
}
