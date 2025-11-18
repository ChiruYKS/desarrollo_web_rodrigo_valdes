package com.tarea4.tarea4.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "aviso_adopcion")
public class AvisoAdopcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDateTime fechaIngreso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comuna_id", nullable = false)
    private Comuna comuna;

    @Column(length = 100)
    private String sector;

    @Column(nullable = false, length = 200)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(length = 15)
    private String celular;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAnimal tipo; // gato / perro

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Integer edad;

    @Enumerated(EnumType.STRING)
    @Column(name = "unidad_medida", nullable = false)
    private UnidadMedida unidadMedida; // a / m

    @Column(name = "fecha_entrega", nullable = false)
    private LocalDateTime fechaEntrega;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @OneToMany(mappedBy = "aviso", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Foto> fotos = new ArrayList<>();

    @OneToMany(mappedBy = "aviso", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ContactarPor> contactos = new ArrayList<>();

    // Nueva tabla que agregamos: nota
    @OneToMany(mappedBy = "aviso", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Nota> notas = new ArrayList<>();

    public enum TipoAnimal {
        gato, perro
    }

    public enum UnidadMedida {
        a, m
    }

    public AvisoAdopcion() {
    }

    public AvisoAdopcion(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public Comuna getComuna() {
        return comuna;
    }

    public String getSector() {
        return sector;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getCelular() {
        return celular;
    }

    public TipoAnimal getTipo() {
        return tipo;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public Integer getEdad() {
        return edad;
    }

    public UnidadMedida getUnidadMedida() {
        return unidadMedida;
    }

    public LocalDateTime getFechaEntrega() {
        return fechaEntrega;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public List<Foto> getFotos() {
        return fotos;
    }

    public List<ContactarPor> getContactos() {
        return contactos;
    }

    public List<Nota> getNotas() {
        return notas;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public void setComuna(Comuna comuna) {
        this.comuna = comuna;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public void setTipo(TipoAnimal tipo) {
        this.tipo = tipo;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public void setUnidadMedida(UnidadMedida unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public void setFechaEntrega(LocalDateTime fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFotos(List<Foto> fotos) {
        this.fotos = fotos;
    }

    public void setContactos(List<ContactarPor> contactos) {
        this.contactos = contactos;
    }

    public void setNotas(List<Nota> notas) {
        this.notas = notas;
    }

    @Override
    public String toString() {
        return "AvisoAdopcion{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", tipo=" + tipo +
                ", cantidad=" + cantidad +
                '}';
    }
    
    private Double promedioNota;

    public Double getPromedioNota() {
        return promedioNota;
    }

    public void setPromedioNota(Double promedioNota) {
        this.promedioNota = promedioNota;
    }
}

