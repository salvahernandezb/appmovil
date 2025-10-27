package com.example.appmovil.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cupos")
public class Cupo {

    @Id //llave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) //generada automaticamente
    private Long id;

    @Column(nullable = false) //no nulo
    private String movil;

    @Column(nullable = false)
    private Double saldo;

    @Column(nullable = false)
    private Double datos;

    @Column(nullable = false)
    private String plataforma;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Constructores
    public Cupo() {}

    public Cupo(String movil, Double saldo, Double datos, String plataforma) {
        this.movil = movil;
        this.saldo = saldo;
        this.datos = datos;
        this.plataforma = plataforma;
    }

    // Getters y Setters para id, saldo, datos, plataforma y usuario
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMovil() {
        return movil;
    }

    public void setMovil(String movil) {
        this.movil = movil;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public Double getDatos() {
        return datos;
    }

    public void setDatos(Double datos) {
        this.datos = datos;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
