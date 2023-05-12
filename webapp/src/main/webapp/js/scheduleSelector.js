document.addEventListener('DOMContentLoaded', function () {
    // =========================  Events on schedule  ==================================================
    // Variables to know if the user is dragging
    // and if the user is dragging to select or unselect blocks
    let isDragging = false;
    let isSelecting = false;

    // Register drag inside schedule
    document.querySelector(".scheduleContainer").addEventListener("mousedown", () => isDragging = true);
    document.body.addEventListener("mouseup", () => isDragging = false);


    // =========================  Events on checkboxes  ==============================================

    document.querySelector("#show-all-times-check").addEventListener("click", toggleAllTimes);

    function toggleAllTimes() {

        // Toggle hidden class for time labels
        let timeLabels = document.querySelectorAll(".timeLabel");

        for(let i = 0; i < timeLabels.length; i++) {
            if (i < 16 || i > 35) {
                timeLabels.item(i).classList.toggle("hidden");
            }
        }

        // Toggle hidden class for time blocks
        document.querySelectorAll(".daySchedule").forEach(daySchedule => {
            let timeBlocks = daySchedule.querySelectorAll(".timeBlock")
            for(let i = 0; i < timeBlocks.length; i++) {
                if (i < 16 || i > 35) {
                    timeBlocks.item(i).classList.toggle("hidden");
                }
            }
        })
    }

    document.querySelector("#show-weekend-check").addEventListener("click", toggleWeekend);
    function toggleWeekend() {
        let days = document.querySelectorAll(".daySchedule");
        days.item(5).classList.toggle("hidden");
        days.item(6).classList.toggle("hidden");
    }

    // =========================  Events on time block  ==============================================
    function handleEnterInBlock(event) {
        // If the user is dragging, only update if the block
        // is in the same state as the first block of the drag
        if (isDragging && (isSelecting === event.target.classList.contains("unselected"))) {
            updateBlockState(event.target);
        }
    }

    function handleDownInBlock(event) {
        // Register if the first block is being selected or unselected
        isSelecting = event.target.classList.contains("unselected");
        updateBlockState(event.target)
    }

    // Updates the value on the form input of the blocks day
    // The orm input has a 64 bit integer, each bit represents the state of a block
    // After updating, the css class is updated
    function updateBlockState(element) {
        // Get input element from father element
        let input = element.parentElement.getElementsByTagName("input")[0];

        // Get bit from element
        let bit = BigInt(element.getAttribute("data-bit"));

        // Negate bit at same index in input
        let newValue = BigInt(input.value) ^ bit;

        // Store the new value in input
        input.value = newValue.toString();

        // Toggle css class
        element.classList.toggle("selected");
        element.classList.toggle("unselected");
    }

    // Add event listeners
    document.querySelectorAll(".timeBlock").forEach(
        element => {
            element.addEventListener("mouseenter", (event) => handleEnterInBlock(event));
            element.addEventListener("mousedown", (event) => handleDownInBlock(event))
        }
    )
});

