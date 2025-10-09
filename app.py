from flask import Flask, render_template, request, redirect, url_for, flash, jsonify
from flask_sqlalchemy import SQLAlchemy
from models import db, AvisoAdopcion, Comuna, Foto, ContactarPor
from math import ceil
from werkzeug.utils import secure_filename
from datetime import datetime
import os
import re

# --- Configuration ---
DB_HOST = "localhost"
DB_PORT = 3306
DB_NAME = "tarea2"
DB_USER = "cc5002"
DB_PASS = "programacionweb"

UPLOAD_FOLDER = 'static/images'
ALLOWED_EXTENSIONS = {'png', 'jpg', 'jpeg', 'gif'}

# --- Flask setup ---
app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = f"mysql+pymysql://{DB_USER}:{DB_PASS}@{DB_HOST}:{DB_PORT}/{DB_NAME}"
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER
app.secret_key = 'supersecretkey'

db = SQLAlchemy(app)

# --- Models ---
class Region(db.Model):
    __tablename__ = "region"
    id = db.Column(db.Integer, primary_key=True)
    nombre = db.Column(db.String(200), nullable=False)
    comunas = db.relationship("Comuna", backref="region", lazy=True)

class Comuna(db.Model):
    __tablename__ = "comuna"
    id = db.Column(db.Integer, primary_key=True)
    nombre = db.Column(db.String(200), nullable=False)
    region_id = db.Column(db.Integer, db.ForeignKey("region.id"), nullable=False)
    avisos = db.relationship("AvisoAdopcion", backref="comuna", lazy=True)

class AvisoAdopcion(db.Model):
    __tablename__ = "aviso_adopcion"
    id = db.Column(db.Integer, primary_key=True)
    fecha_ingreso = db.Column(db.DateTime, nullable=False)
    comuna_id = db.Column(db.Integer, db.ForeignKey("comuna.id"), nullable=False)
    sector = db.Column(db.String(100))
    nombre = db.Column(db.String(200), nullable=False)
    email = db.Column(db.String(100), nullable=False)
    celular = db.Column(db.String(15))
    tipo = db.Column(db.Enum('gato', 'perro'), nullable=False)
    cantidad = db.Column(db.Integer, nullable=False)
    edad = db.Column(db.Integer, nullable=False)
    unidad_medida = db.Column(db.Enum('a', 'm'), nullable=False)
    fecha_entrega = db.Column(db.DateTime, nullable=False)
    descripcion = db.Column(db.Text(500))
    fotos = db.relationship("Foto", backref="aviso", lazy=True)
    contactos = db.relationship("ContactarPor", backref="aviso", lazy=True)

class Foto(db.Model):
    __tablename__ = "foto"
    id = db.Column(db.Integer, primary_key=True)
    ruta_archivo = db.Column(db.String(300), nullable=False)
    nombre_archivo = db.Column(db.String(300), nullable=False)
    actividad_id = db.Column(db.Integer, db.ForeignKey("aviso_adopcion.id"), nullable=False)

class ContactarPor(db.Model):
    __tablename__ = "contactar_por"
    id = db.Column(db.Integer, primary_key=True)
    nombre = db.Column(db.Enum('whatsapp', 'telegram', 'X', 'instagram', 'tiktok', 'otra'), nullable=False)
    identificador = db.Column(db.String(150), nullable=False)
    actividad_id = db.Column(db.Integer, db.ForeignKey("aviso_adopcion.id"), nullable=False)

# --- Helpers ---
def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS

# --- Routes ---
@app.route('/')
def home():
    return redirect(url_for('portada'))

@app.route('/portada/')
def portada():
    avisos = AvisoAdopcion.query.order_by(AvisoAdopcion.fecha_ingreso.desc()).limit(5).all()
    regiones = Region.query.all() 
    return render_template("index.html", avisos=avisos, regiones=regiones)

@app.route('/agregar_aviso/')
def agregar_aviso_form():
    regiones = Region.query.all()
    # Para cada región, traemos sus comunas
    regiones_data = []
    for r in regiones:
        regiones_data.append({
            "id": r.id,
            "nombre": r.nombre,
            "comunas": [{"id": c.id, "nombre": c.nombre} for c in r.comunas]
        })
    return render_template("p1.html", regiones_data=regiones_data)

@app.route('/add_aviso/', methods=['POST'])
def add_aviso():
    errors = []

    try:
        # --- Obtener datos del formulario ---
        comuna_id = request.form.get('comuna')
        sector = request.form.get('sector', '').strip()
        nombre = request.form.get('nombre', '').strip()
        email = request.form.get('email', '').strip()
        celular = request.form.get('tel', '').strip()
        contactar_por_list = request.form.getlist('contactSelect')
        identificador_list = request.form.getlist('contactInput')
        tipo = request.form.get('simpleSelect', '').lower()
        cantidad = request.form.get('int1')
        edad = request.form.get('int2')
        unidad_medida = request.form.get('simpleSelect2')
        fecha_entrega_str = request.form.get('entrega')
        descripcion = request.form.get('descripcion', '').strip()

        # --- Validación ---
        if not comuna_id:
            errors.append("Debe seleccionar una comuna.")
        if sector and len(sector) > 100:
            errors.append("El nombre del sector no puede exceder 100 caracteres.")
        if not (3 <= len(nombre) <= 100):
            errors.append("El nombre debe tener entre 3 y 100 caracteres.")
        if not re.match(r'^[^\s@]+@[^\s@]+\.[^\s@]+$', email):
            errors.append("Correo inválido.")
        if celular and not re.match(r'^\+\d{3}\.\d{8}$', celular):
            errors.append("Número de celular inválido (+XXX.XXXXXXXX).")
        if tipo not in ['gato', 'perro']:
            errors.append("Tipo de animal inválido.")
        try:
            cantidad = int(cantidad)
            if cantidad < 1:
                errors.append("Cantidad debe ser mayor a 0.")
        except:
            errors.append("Cantidad debe ser un número entero.")
        try:
            edad = int(edad)
            if edad < 0:
                errors.append("Edad inválida.")
        except:
            errors.append("Edad debe ser un número entero.")
        if unidad_medida not in ['Años', 'Meses']:
            errors.append("Unidad de medida inválida.")
        try:
            fecha_entrega = datetime.fromisoformat(fecha_entrega_str)
        except:
            errors.append("Fecha de entrega inválida.")

        # Validar contacto si se seleccionó
        contactos = []
        for medio, ident in zip(contactar_por_list, identificador_list):
            if medio and ident:
                if len(ident) < 4 or len(ident) > 50:
                    errors.append(f"ID/URL inválido para {medio}.")
                else:
                    contactos.append((medio.lower(), ident.strip()))

        # Validar archivos
        files = request.files.getlist('fotos')
        for file in files:
            if file.filename and not allowed_file(file.filename):
                errors.append(f"Archivo {file.filename} no permitido.")

        # --- Si hay errores, renderizamos de nuevo ---
        if errors:
            for e in errors:
                flash(str(e), "danger")
                regiones = Region.query.all()
                regiones_data = []
                for r in regiones:
                    regiones_data.append({
                        "id": r.id,
                        "nombre": r.nombre,
                        "comunas": [{"id": c.id, "nombre": c.nombre} for c in r.comunas]
                    })
                return render_template("p1.html", regiones_data=regiones_data)

        # --- Insertar aviso_adopcion ---
        aviso = AvisoAdopcion(
            fecha_ingreso=datetime.now(),
            comuna_id=comuna_id,
            sector=sector,
            nombre=nombre,
            email=email,
            celular=celular,
            tipo=tipo,
            cantidad=cantidad,
            edad=edad,
            unidad_medida='a' if unidad_medida == 'Años' else 'm',
            fecha_entrega=fecha_entrega,
            descripcion=descripcion
        )
        db.session.add(aviso)
        db.session.commit()  # necesario para obtener aviso.id

        # --- Insertar fotos ---
        os.makedirs(app.config['UPLOAD_FOLDER'], exist_ok=True)
        for file in files[:5]:  # máximo 5
            if file and file.filename:
                filename = secure_filename(file.filename)
                filepath = os.path.join(app.config['UPLOAD_FOLDER'], filename)
                file.save(filepath)
                foto = Foto(ruta_archivo=f"images/{filename}", nombre_archivo=filename, actividad_id=aviso.id)
                db.session.add(foto)
        db.session.commit()

        # --- Insertar contactos ---
        for medio, ident in contactos:
            contacto = ContactarPor(nombre=medio, identificador=ident, actividad_id=aviso.id)
            db.session.add(contacto)
        db.session.commit()

        flash("Aviso agregado correctamente!", "success")
        return redirect(url_for('portada'))

    except Exception as e:
        db.session.rollback()
        flash(f"Ocurrió un error: {str(e)}", "danger")
        regiones = Region.query.all()
        return render_template("p1.html", regiones=regiones)

@app.route('/listado_avisos/')
def listado_avisos():
    page = request.args.get('page', 1, type=int)
    per_page = 5

    # Query con JOIN a Comuna
    query = AvisoAdopcion.query.join(Comuna).order_by(AvisoAdopcion.fecha_ingreso.desc())
    total = query.count()
    avisos = query.offset((page-1)*per_page).limit(per_page).all()

    total_pages = ceil(total / per_page)
    return render_template(
        "p2.html",
        avisos=avisos,
        page=page,
        total_pages=total_pages
    )

# Endpoint para obtener detalle de un aviso vía AJAX
@app.route('/detalle_aviso/<int:aviso_id>/')
def detalle_aviso(aviso_id):
    aviso = AvisoAdopcion.query.get_or_404(aviso_id)

    fotos = [foto.ruta_archivo for foto in aviso.fotos]
    contactos = [f"{c.nombre}: {c.identificador}" for c in aviso.contactos]

    data = {
        "fecha_ingreso": aviso.fecha_ingreso.strftime("%Y-%m-%d %H:%M"),
        "fecha_entrega": aviso.fecha_entrega.strftime("%Y-%m-%d %H:%M"),
        "comuna": aviso.comuna.nombre,
        "sector": aviso.sector,
        "cantidad_tipo_edad": f"{aviso.cantidad}, {aviso.tipo.capitalize()}, {aviso.edad} {'años' if aviso.unidad_medida=='a' else 'meses'}",
        "contactos": contactos,
        "fotos": fotos
    }

    return jsonify(data)



# --- Run server ---
if __name__ == "__main__":
    # Ensure upload folder exists
    os.makedirs(UPLOAD_FOLDER, exist_ok=True)
    app.run(debug=True)



    
    
