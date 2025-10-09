    const rows = document.querySelectorAll("#listingTable tr:not(:first-child)");
    const listingTable = document.getElementById("listingTable");
    const detailView = document.getElementById("detailView");
    const detailContent = document.getElementById("detailContent");

    rows.forEach(row => {
      row.addEventListener("click", () => {
        const cells = row.querySelectorAll("td");
        const img = cells[6].querySelector("img");

        detailContent.innerHTML = `
          <table>
            <tr><th>Fecha Publicación:</th><td>${cells[0].innerText}</td></tr>
            <tr><th>Fecha Entrega:</th><td>${cells[1].innerText}</td></tr>
            <tr><th>Comuna:</th><td>${cells[2].innerText}</td></tr>
            <tr><th>Sector:</th><td>${cells[3].innerText}</td></tr>
            <tr><th>Cantidad/Tipo/Edad:</th><td>${cells[4].innerText}</td></tr>
            <tr><th>Contacto:</th><td>${cells[5].innerText}</td></tr>
            <tr><th>Foto:</th>
              <td>
                <img src="${img.src}" class="pet-img" onclick="expandImage(this)">
                <button class="btn close-img" onclick="shrinkImage(this)">Cerrar imagen</button>
              </td>
            </tr>
          </table>
        `;

        listingTable.style.display = "none";
        detailView.style.display = "block";
      });
    });

    function showListing() {
      detailView.style.display = "none";
      listingTable.style.display = "table";
    }

    function expandImage(img) {
      img.classList.add("img-expanded");
      img.nextElementSibling.style.display = "inline-block";
    }

    function shrinkImage(btn) {
      const img = btn.previousElementSibling;
      img.classList.remove("img-expanded");
      btn.style.display = "none";
    }