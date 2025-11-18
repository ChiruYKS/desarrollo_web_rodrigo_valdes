package com.tarea4.tarea4.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comuna")
public class Comuna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 200)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    @OneToMany(mappedBy = "comuna", fetch = FetchType.LAZY)
    private List<AvisoAdopcion> avisos = new ArrayList<>();

    public Comuna() {
    }

    public Comuna(Integer id, String nombre, Region region) {
        this.id = id;
        this.nombre = nombre;
        this.region = region;
    }

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Region getRegion() {
        return region;
    }

    public List<AvisoAdopcion> getAvisos() {
        return avisos;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public void setAvisos(List<AvisoAdopcion> avisos) {
        this.avisos = avisos;
    }

    @Override
    public String toString() {
        return "Comuna{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
