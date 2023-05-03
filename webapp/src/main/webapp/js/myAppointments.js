document.addEventListener('DOMContentLoaded', function () {
    // Get all tab content
    var tabContents = document.querySelectorAll('.tabContent');

    // Hide all tab content except for the active one
    for (var i = 0; i < tabContents.length; i++) {
        if (!tabContents[i].classList.contains('active')) {
            tabContents[i].style.display = 'none';
        }
    }

    // Get all tab links
    var tabLinks = document.querySelectorAll('li.nav-item a.tab');

    // Add click event listeners to each tab link
    for (i = 0; i < tabLinks.length; i++) {
        tabLinks[i].addEventListener('click', function (e) {
            e.preventDefault();

            // Remove 'active', 'bg-primary' and 'text-white' classes from all tab links
            for (var j = 0; j < tabLinks.length; j++) {
                tabLinks[j].classList.remove('active');
                tabLinks[j].classList.remove('bg-primary');
                tabLinks[j].classList.remove('text-white');
            }

            // Add 'active', 'bg-primary' and 'text-white' classes to the clicked tab link
            this.classList.add('active');
            this.classList.add('bg-primary');
            this.classList.add('text-white');

            // Get the target tab content and hide all other tab content
            var target = this.getAttribute('href');
            for (j = 0; j < tabContents.length; j++) {
                if (tabContents[j].getAttribute('id') === target.substring(1)) {
                    tabContents[j].style.display = 'block';
                } else {
                    tabContents[j].style.display = 'none';
                }
            }

            // get all the <a class="detailed-link"> tags in the document
            var links = document.querySelectorAll("a.detailed-link");

            // loop through all the links and modify their href attributes
            for (var k = 0; k < links.length; k++) {

                // get the original href attribute
                var originalHref = links[k].getAttribute("href");
                originalHref = originalHref.replace(/[\?&]selected_tab=\d+/, "");

                // get the selected tab's index
                var selectedTab = document.querySelector("#nav .nav-link.active");
                var selectedIndex = Array.from(selectedTab.parentNode.parentNode.children).indexOf(selectedTab.parentNode);

                // modify the href attribute to include the query parameters
                var newHref = originalHref + "&selected_tab=" + encodeURIComponent(selectedIndex);
                links[k].setAttribute("href", newHref);
            }


            // Change the get-form, input with id selected_tab value to the current tab index
            document.getElementById('selected_tab').value = Array.from(this.parentNode.parentNode.children).indexOf(this.parentNode);
        });
    }
});
// Get all buttons with the class name and add the event listener to each of them
document.querySelectorAll(".post-button").forEach(function (button) {
    button.addEventListener("click", function (event) {
        event.preventDefault();
        // get the values from the get-form
        var from = document.getElementById("get-form").elements.namedItem("from").value;
        var to = document.getElementById("get-form").elements.namedItem("to").value;

        // get the selected tab's index
        var selectedTab = document.querySelector("#nav .nav-link.active");
        var selectedIndex = Array.from(selectedTab.parentNode.parentNode.children).indexOf(selectedTab.parentNode);


        // modify the action attribute of the post-form to include query parameters
        var postForm = this.parentNode;
        console.log(postForm);
        var postAction = postForm.getAttribute("action");
        postAction += "&from=" + encodeURIComponent(from) + "&to=" + encodeURIComponent(to) + "&selected_tab=" + encodeURIComponent(selectedIndex);
        postForm.setAttribute("action", postAction);

        // submit the post-form
        postForm.submit();
    });
});
