package com.example.appmovil.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.appmovil.model.Cupo;
import com.example.appmovil.model.Usuario;
import java.util.Optional;

@Repository
public interface CupoRepository extends JpaRepository<Cupo, Long> {
    Optional<Cupo> findByUsuarioAndMovil(Usuario usuario, String movil);
}
