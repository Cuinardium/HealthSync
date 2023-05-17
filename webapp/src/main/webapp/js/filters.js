function clearFilters() {
    document.getElementById('city-select').selectedIndex = 0;
    document.getElementById('specialty-select').selectedIndex = 0;
    document.getElementById('health-insurance-select').selectedIndex = 0;
    document.getElementById('name-input').value = '';
    document.getElementById('date').value = '';
    document.getElementById('from-select').selectedIndex = -1;
    document.getElementById('to-select').selectedIndex = -1;
}