CREATE TABLE IF NOT EXISTS region (
  id INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(200) NOT NULL,
  PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS comuna (
  id INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(200) NOT NULL,
  region_id INT NOT NULL,
  PRIMARY KEY (id),
  INDEX fk_comuna_region1_idx (region_id ASC),
  CONSTRAINT fk_comuna_region1
    FOREIGN KEY (region_id)
    REFERENCES region (id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS aviso_adopcion (
  id INT NOT NULL AUTO_INCREMENT,
  fecha_ingreso DATETIME NOT NULL,
  comuna_id INT NOT NULL,
  sector VARCHAR(100),
  nombre VARCHAR(200) NOT NULL,
  email VARCHAR(100) NOT NULL,
  celular VARCHAR(15),
  tipo ENUM('gato', 'perro') NOT NULL,
  cantidad INT NOT NULL,
  edad INT NOT NULL,
  unidad_medida ENUM('a', 'm') NOT NULL,
  fecha_entrega DATETIME NOT NULL,
  descripcion TEXT(500),
  PRIMARY KEY (id),
  INDEX fk_aviso_comuna1_idx (comuna_id ASC),
  CONSTRAINT fk_aviso_comuna1
    FOREIGN KEY (comuna_id)
    REFERENCES comuna (id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS foto (
  id INT NOT NULL AUTO_INCREMENT,
  ruta_archivo VARCHAR(300) NOT NULL,
  nombre_archivo VARCHAR(300) NOT NULL,
  actividad_id INT NOT NULL,
  PRIMARY KEY (id),
  INDEX fk_foto_aviso1_idx (actividad_id ASC),
  CONSTRAINT fk_foto_aviso1
    FOREIGN KEY (actividad_id)
    REFERENCES aviso_adopcion (id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS contactar_por (
  id INT NOT NULL AUTO_INCREMENT,
  nombre ENUM('whatsapp', 'telegram', 'X', 'instagram', 'tiktok', 'otra') NOT NULL,
  identificador VARCHAR(150) NOT NULL,
  actividad_id INT NOT NULL,
  PRIMARY KEY (id),
  INDEX fk_contactar_por_aviso1_idx (actividad_id ASC),
  CONSTRAINT fk_contactar_por_aviso1
    FOREIGN KEY (actividad_id)
    REFERENCES aviso_adopcion (id)
) ENGINE = InnoDB;
