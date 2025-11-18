package com.tarea4.tarea4.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "contactar_por")
public class ContactarPor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MedioContacto nombre;

    @Column(nullable = false, length = 150)
    private String identificador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actividad_id", nullable = false)
    private AvisoAdopcion aviso;

    public enum MedioContacto {
        whatsapp, telegram, X, instagram, tiktok, otra
    }

    public ContactarPor() {
    }

    public Integer getId() {
        return id;
    }

    public MedioContacto getNombre() {
        return nombre;
    }

    public String getIdentificador() {
        return identificador;
    }

    public AvisoAdopcion getAviso() {
        return aviso;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNombre(MedioContacto nombre) {
        this.nombre = nombre;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public void setAviso(AvisoAdopcion aviso) {
        this.aviso = aviso;
    }
}

