package com.example.appmovil.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity 
@Table(name = "usuarios")
public class Usuario {

    @Id //llave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) //generada automaticamente
    private Long id;

    @Column(nullable = false) //no nulo 
    private String nombre;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL) //relacion uno a muchos con Cupo
    @JsonIgnore // prevent infinite recursion when serializing Usuario <-> Cupo relationship
    private List<Cupo> cupos;

    // Constructores
    public Usuario() {}

    public Usuario(String nombre) {
        this.nombre = nombre;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Cupo> getCupos() {
        return cupos;
    }

    public void setCupos(List<Cupo> cupos) {
        this.cupos = cupos;
    }
}
