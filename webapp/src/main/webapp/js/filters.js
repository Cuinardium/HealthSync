function clearFilters() {
    document.getElementById('city-select').selectedIndex = 0;
    document.getElementById('specialty-select').selectedIndex = 0;
    document.getElementById('health-insurance-select').selectedIndex = 0;
    document.getElementById('name-input').value = '';
    document.getElementById('date').value = '';

    let fromSelect = document.getElementById('from-select');
    let toSelect = document.getElementById('to-select');

    if (fromSelect) {
        fromSelect.selectedIndex = -1;
    }

    if (toSelect) {
        toSelect.selectedIndex = -1;
    }

    document.getElementById('min-rating').value = '0';
}

function updateStar(event) {
    let star = event.target.parentNode;
    let starIndex = Number(star.getAttribute("data-index"));

    console.log(starIndex)

    // Update css class of stars
    let stars = document.getElementById("rating-selector").getElementsByTagName("div");

    for (let i = 0; i < stars.length; i++) {
        if (i < starIndex) {
            stars[i].classList.add("selected");
            stars[i].classList.remove("unselected");
        } else {
            stars[i].classList.add("unselected");
            stars[i].classList.remove("selected");
        }
    }

    // Update input field
    document.getElementById("min-rating").value = starIndex.toString();
    document.getElementById("min-rating").onchange();
}

document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".star").forEach( star => {
        star.addEventListener("click", updateStar)
    });
});
