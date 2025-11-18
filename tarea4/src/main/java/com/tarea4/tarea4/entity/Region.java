package com.tarea4.tarea4.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "region")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    @OneToMany(mappedBy = "region", fetch = FetchType.LAZY)
    private List<Comuna> comunas = new ArrayList<>();

    public Region() {
    }

    public Region(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Comuna> getComunas() {
        return comunas;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setComunas(List<Comuna> comunas) {
        this.comunas = comunas;
    }

    @Override
    public String toString() {
        return "Region{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
