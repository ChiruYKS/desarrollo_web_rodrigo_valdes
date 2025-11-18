package com.tarea4.tarea4.repository;

import com.tarea4.tarea4.entity.ContactarPor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactarPorRepository extends JpaRepository<ContactarPor, Integer> {

    // Contactos asociados a un aviso
    List<ContactarPor> findByActividadId(Integer avisoId);
}

