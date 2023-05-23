function updateTab(e, selectedIndex) {
    e.preventDefault();

    // Set selectedTab
    selectedTab = selectedIndex + 1

    // Get all tab links
    const tabLinks = document.querySelectorAll('li.nav-item a.tab');

    // Remove 'active', 'bg-primary' and 'text-white' classes from all tab links
    for (let j = 0; j < tabLinks.length; j++) {
        tabLinks[j].classList.remove('active');
        tabLinks[j].classList.remove('bg-primary');
        tabLinks[j].classList.remove('text-white');
    }

    // Add 'active', 'bg-primary' and 'text-white' classes to the clicked tab link
    tabLinks[selectedIndex].classList.add('active');
    tabLinks[selectedIndex].classList.add('bg-primary');
    tabLinks[selectedIndex].classList.add('text-white');

    // Get all tab content
    const tabContents = document.querySelectorAll('.tabContent');

    // Get the target tab content and hide all other tab content
    const target = e.target.getAttribute('href');
    for (let j = 0; j < tabContents.length; j++) {
        if (tabContents[j].getAttribute('id') === target.substring(1)) {
            tabContents[j].style.display = 'block';
        } else {
            tabContents[j].style.display = 'none';
        }
    }

    // get all the <a class="detailed-link"> tags in the document
    const links = document.querySelectorAll("a.detailed-link");

    // loop through all the links and modify their href attributes
    for (let k = 0; k < links.length; k++) {

        // get the original href attribute
        let originalHref = links[k].getAttribute("href");
        originalHref = originalHref.replace(/[?&]selected_tab=\d+/, "");

        // modify the href attribute to include the query parameters
        const newHref = originalHref + "&selected_tab=" + encodeURIComponent(selectedTab);
        links[k].setAttribute("href", newHref);
    }
}

function addListeners() {
    // Get all tab content
    const tabContents = document.querySelectorAll('.tabContent');

    // Hide all tab content except for the active one
    for (let i = 0; i < tabContents.length; i++) {
        if (!tabContents[i].classList.contains('active')) {
            tabContents[i].style.display = 'none';
        }
    }

    // Get all tab links
    const tabLinks = document.querySelectorAll('li.nav-item a.tab');

    // Add click event listeners to each tab link
    for (let i = 0; i < tabLinks.length; i++) {
        tabLinks[i].addEventListener('click', e => updateTab(e, i));
    }
}


document.addEventListener('DOMContentLoaded', addListeners);

