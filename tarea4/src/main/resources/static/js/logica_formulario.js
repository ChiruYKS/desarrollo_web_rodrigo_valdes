//Generar Regiones

    window.addEventListener("load", function() {
      const regionSelect = document.getElementById("region");

      region_comuna.regiones.forEach(region => {
        const option = document.createElement("option");
        option.value = region.numero;   // value is region number
        option.text = region.nombre;    // visible text
        regionSelect.appendChild(option);
      });
    });

//Logica dependencia Region->Comuna

    function updateComunas() {
      const regionSelect = document.getElementById("region");
      const comunaSelect = document.getElementById("comuna");
      const selectedRegionId = parseInt(regionSelect.value);

      comunaSelect.innerHTML = '<option value="">--Seleccione una comuna--</option>';

      if (!selectedRegionId) return;

      // Buscar la región en el template data
      const regionesData = {{ regiones_data|tojson }};
      const region = regionesData.find(r => r.id === selectedRegionId);
      if (region) {
        region.comunas.forEach(c => {
          const option = document.createElement("option");
          option.value = c.id;
          option.text = c.nombre;
          comunaSelect.appendChild(option);
        });
      }
    }

//Funcion que implementa el mecanismo de contacto

    function updateContact() {
      const select = document.getElementById("contactSelect");
      const contactRow = document.getElementById("contactRow");
      const contactLetter = document.getElementById("contactLetter");
      const contactInput = document.getElementById("contactInput");
      

      if (select.value !== "") {
        contactRow.style.display = "table-row";
        contactLetter.textContent = select.options[select.selectedIndex].text;
        contactInput.value = ""; // reset input
      } else {
        contactRow.style.display = "none";
      }
    }

//Funcion que controla la Fecha

    window.addEventListener("load", function() {
      const appointmentInput = document.getElementById("entrega");

      const now = new Date();
      now.setHours(now.getHours() + 3); // add 3 hours

      // Formato YYYY-MM-DDTHH:MM

      const year = now.getFullYear();
      const month = String(now.getMonth() + 1).padStart(2, "0");
      const day = String(now.getDate()).padStart(2, "0");
      const hours = String(now.getHours()).padStart(2, "0");
      const minutes = String(now.getMinutes()).padStart(2, "0");

      const prefill = `${year}-${month}-${day}T${hours}:${minutes}`;
      appointmentInput.value = prefill;

      appointmentInput.min = prefill;
    });

    const maxFiles = 5;
    const fileContainer = document.getElementById("fileContainer");
    const addFileBtn = document.getElementById("addFileBtn");

    addFileBtn.addEventListener("click", () => {
      const currentInputs = fileContainer.querySelectorAll('input[type="file"]');
  
      if (currentInputs.length < maxFiles) {
        const newInput = document.createElement("input");
        newInput.type = "file";
        newInput.name = "fotos";
        newInput.accept = "image/*";
        newInput.style.display = "block";
        newInput.style.marginTop = "5px";
        fileContainer.appendChild(newInput);
      } else {
        alert("No puede agregar más de 5 imágenes.");
      }
    });

//Funcion de Validacion

    function validateForm() {
      console.log("validateForm called"); // debug
      const region = document.getElementById("region").value;
      const comuna = document.getElementById("comuna").value;
      const sector = document.getElementById("sector").value;
      const nombre = document.getElementById("nombre").value;
      const email = document.getElementById("email").value;
      const tel = document.getElementById("tel").value;

      const appointmentInput = document.getElementById("entrega");
      const selectedDate = new Date(appointmentInput.value);
      const minDate = new Date(appointmentInput.min);

      if (selectedDate < minDate) {
        alert("La fecha y hora seleccionadas deben ser igual o posteriores a la prellenada.");
        return false;
      }
      
//Validaciones para tabla Ubicacion

      if (region === "") {
        alert("Debe seleccionar una región.");
        return false; // prevent submit
      }

      if (comuna === "") {
        alert("Debe seleccionar una comuna.");
        return false; // prevent submit
      }

      if (sector.length > 100) {
        alert("El nombre del sector no puede exceder 100 caracteres.");
        return false;
      }


//Validacion para tabla Contacto

      if (nombre.length < 3 || nombre.length > 100) {
        alert("El nombre debe tener entre 3 y 100 caracteres.");
        return false;
      }
      
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailRegex.test(email)) {
        alert("Ingrese un correo electrónico válido (ejemplo: usuario@dominio.com).");
        return false;
      }

      const phoneRegex = /^\+\d{3}\.\d{8}$/;
      if (!phoneRegex.test(tel)) {
        alert("Ingrese un número de celular válido en formato +XXX.XXXXXXXX");
        return false;
      }

      const contactRow = document.getElementById("contactRow");
      if (contactRow.style.display !== "none") {
        const contactValue = document.getElementById("contactInput").value;
        if (contactValue.length < 4 || contactValue.length > 50) {
          alert("El campo ID/URL debe tener entre 4 y 50 caracteres.");
          return false;
        }
      }

//Validacion para dropdowns tabla Mascota
      
     const simpleSelect = document.getElementById("simpleSelect").value;

     if (simpleSelect === "") {
       alert("Debe seleccionar un tipo de animal.");
       return false; // prevent form submission
     }

     const simpleSelect2 = document.getElementById("simpleSelect2").value;

     if (simpleSelect2 === "") {
       alert("Debe seleccionar una medida.");
       return false; // prevent form submission
     }


     const intFields = ["int1", "int2"];
     for (let id of intFields) {
       const value = document.getElementById(id).value;
       if (!/^\d+$/.test(value) || parseInt(value) < 1) {
         alert(`Ingrese un valor entero para Cantidad/Edad (1, 2, 3, ...).`);
         return false;
       }
     }

     
     if (selectedDate < minDate) {
       alert("La fecha y hora seleccionadas deben ser igual o posteriores a la prellenada.");
       return false;
     }
    
     const userConfirmed = confirm("¿Está seguro que desea agregar este aviso de adopción?");
     if (!userConfirmed) {
      return false; // stop submission
     }

  // Optional: show thank you message after user confirms
     alert("Hemos recibido la información de adopción, muchas gracias y suerte");

     return true; // allow form submission
   }

