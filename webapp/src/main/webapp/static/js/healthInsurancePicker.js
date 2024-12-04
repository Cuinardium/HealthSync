document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".healthInsuranceItem").forEach(item => {
        item.addEventListener("click", function (event) {
            let index = Number(event.target.getAttribute("data-index"));


            document.querySelector("#chipsContainer").children.item(index).classList.toggle("hidden");
            event.target.classList.toggle("hidden");


            let menuItems = document.querySelector("#healthInsuranceDropdownMenu").children;
            let areAllHidden = Array.from(menuItems).every(menuItem => menuItem.children[0].classList.contains("hidden"));

            document.querySelector("#healthInsuranceDropdownLabel").classList.add("hidden");

            if (areAllHidden) {
                document.querySelector("#healthInsuranceDropdown").classList.add("hidden");
            }

            document.querySelector("#healthInsuranceSelect").getElementsByTagName("option").item(index).selected = true;
        });
    });

    document.querySelectorAll(".chipClose").forEach(button => {
        button.addEventListener("click", function (event) {
            let index = Number(event.target.getAttribute("data-index"));

            document.querySelector("#healthInsuranceDropdownMenu").children.item(index).children[0].classList.toggle("hidden");
            event.target.parentElement.classList.toggle("hidden");

            let menuItems = document.querySelector("#healthInsuranceDropdownMenu").children;
            let areAllHidden = Array.from(menuItems).every(menuItem => menuItem.children[0].classList.contains("hidden"));

            let chips = document.querySelector("#chipsContainer").querySelectorAll(".chip");
            let areAllChipsHidden = Array.from(chips).every(chip => chip.classList.contains("hidden"));

            if (areAllChipsHidden) {
                document.querySelector("#healthInsuranceDropdownLabel").classList.remove("hidden")
            }

            if (!areAllHidden) {
                document.querySelector("#healthInsuranceDropdown").classList.remove("hidden")
            }

            document.querySelector("#healthInsuranceSelect").getElementsByTagName("option").item(index).selected = false;
        });
    });
})