
document.addEventListener("DOMContentLoaded", function () {
    function updateStar(event) {
        let star = event.target.parentNode;
        console.log(star)
        let starIndex = Number(star.getAttribute("data-index"));
        console.log(starIndex)

        // Update css class of stars
        let stars = document.querySelectorAll(".starContainer")[0].getElementsByTagName("div");

        for (let i = 0; i < stars.length; i++) {
            if (i < starIndex) {
                stars[i].classList.add("selected");
                stars[i].classList.remove("unselected");
                console.log(stars[i])
            } else {
                stars[i].classList.add("unselected");
                stars[i].classList.remove("selected");
                console.log(stars[i])
            }
        }

        // Update input field
        document.getElementById("ratingInput").value = starIndex.toString();
        console.log(document.getElementById("ratingInput").value)
    }

    document.querySelectorAll(".star").forEach( star => {
        star.addEventListener("click", updateStar)
    });
})
