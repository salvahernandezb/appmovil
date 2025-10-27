package com.example.appmovil.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.appmovil.model.Usuario;
import com.example.appmovil.repository.UsuarioRepository;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario findByNombre(String nombre) {
        return usuarioRepository.findByNombre(nombre).orElse(null);
    }

    public Usuario createUsuario(String nombre) {
        Usuario usuario = new Usuario(nombre);
        return usuarioRepository.save(usuario);
    }

    public Usuario findOrCreateByNombre(String nombre) {
        Optional<Usuario> existingUsuario = usuarioRepository.findByNombre(nombre);
        return existingUsuario.orElseGet(() -> createUsuario(nombre));
    }
}
