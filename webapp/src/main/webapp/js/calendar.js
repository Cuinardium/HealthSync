(function($) {

	"use strict";

	// Setup the calendar with the current date
$(document).ready(function(){
    var date = new Date();
    var today = date.getDate();
    // Set click handlers for DOM elements
    $(".right-button").click({date: date}, nextYear);
    $(".left-button").click({date: date}, prevYear);
    $(".month").click({date: date}, monthClick);
    // Set current month as active
    $(".months-row").children().eq(date.getMonth()).addClass("active-month");
    initCalendar(date);
    var appointments = checkAppointments(today, date.getMonth()+1, date.getFullYear());
    showAppointments(appointments, months[date.getMonth()], today);
});

// Initialize the calendar by appending the HTML dates
function initCalendar(date) {
    $(".tbody").empty();
    $(".appointments-container").empty();
    var calendarDays = $(".tbody");
    var month = date.getMonth();
    var year = date.getFullYear();
    var dayCount = daysInMonth(month, year);
    var row = $("<tr class='table-row'></tr>");
    var today = date.getDate();
    // Set date to 1 to find the first day of the month
    date.setDate(1);
    var firstDay = date.getDay();
    // 35+firstDay is the number of date elements to be added to the dates table
    // 35 is from (7 days in a week) * (up to 5 rows of dates in a month)
    for(var i=0; i<35+firstDay; i++) {
        // Since some of the elements will be blank,
        // need to calculate actual date from index
        var day = i-firstDay+1;
        // If it is a sunday, make a new row
        if(i%7===0) {
            calendarDays.append(row);
            row = $("<tr class='table-row'></tr>");
        }
        // if current index isn't a day in this month, make it blank
        if(i < firstDay || day > dayCount) {
            var currDate = $("<td class='table-date nil'>"+"</td>");
            row.append(currDate);
        }
        else {
            var currDate = $("<td class='table-date'>"+day+"</td>");
            var appointments = checkAppointments(day, month+1, year);
            if(today===day && $(".active-date").length===0) {
                currDate.addClass("active-date");
                showAppointments(appointments, months[month], day);
            }
            // If this date has any appointments, style it with .appointment-date
            if(appointments.length!==0) {
                currDate.addClass("appointment-date");
            }
            // Set onClick handler for clicking a date
            currDate.click({appointments: appointments, month: months[month], day:day}, dateClick);
            row.append(currDate);
        }
    }
    // Append the last row and set the current year
    calendarDays.append(row);
    $(".year").text(year);
}

// Get the number of days in a given month/year
function daysInMonth(month, year) {
    var monthStart = new Date(year, month, 1);
    var monthEnd = new Date(year, month + 1, 1);
    return (monthEnd - monthStart) / (1000 * 60 * 60 * 24);
}

// appointment handler for when a date is clicked
function dateClick(appointment) {
    $(".appointments-container").show(250);
    $("#dialog").hide(250);
    $(".active-date").removeClass("active-date");
    $(this).addClass("active-date");
    let date = new Date($(".year").text(), months.indexOf(appointment.data.month), appointment.data.day);
    document.getElementById("date").value = date.toLocaleDateString("fr-CA");
    showAppointments(appointment.data.appointments, appointment.data.month, appointment.data.day);
};

// Appointment handler for when a month is clicked
function monthClick(appointment){
    $(".appointments-container").show(250);
    $("#dialog").hide(250);
    var date = appointment.data.date;
    $(".active-month").removeClass("active-month");
    $(this).addClass("active-month");
    var newMonth = $(".month").index(this);
    date.setMonth(newMonth);
    initCalendar(date);
}

// appointment handler for when the year right-button is clicked
function nextYear(appointment) {
    $("#dialog").hide(250);
    var date = appointment.data.date;
    var newYear = date.getFullYear()+1;
    $("year").html(newYear);
    date.setFullYear(newYear);
    initCalendar(date);
}

// appointment handler for when the year left-button is clicked
function prevYear(appointment) {
    $("#dialog").hide(250);
    var date = appointment.data.date;
    var newYear = date.getFullYear()-1;
    $("year").html(newYear);
    date.setFullYear(newYear);
    initCalendar(date);
}

// Display all appointments of the selected date in card views
function showAppointments(appointments, month, day) {
    // Clear the dates container
    $(".appointments-container").empty();
    $(".appointments-container").show(250);
    // If there are no appointments for this date, notify the user
    if (appointments.length === 0) {
        var appointmentCard = $("<div class='card noAppointments'></div>");
        var appointmentTime = $("<div class='appointment-time'>" + document.getElementById("noAppointmentsAvailableMsg").value + "</div>");
        $(appointmentCard).append(appointmentTime);
        $(".appointments-container").append(appointmentCard);
    } else {
        // Go through and add each appointment as a card to the appointments container
        for (var i = 0; i < appointments.length; i++) {
            var appointmentCard = $("<div class='btn btn-light appointment-button' onclick='openModal(); setBlock(\"" + appointments[i]["time"] + "\");'></div>");
            var appointmentTime = $("<div class='appointment-time'>" + appointments[i]["time"] + "</div>");
            $(appointmentCard).append(appointmentTime);
            $(".appointments-container").append(appointmentCard);
        }
    }
}

// Checks if a specific date has any appointments
function checkAppointments(day, month, year) {
    var appointments = [];
    for(var i=0; i<appointmentData["appointments"].length; i++) {
        var appointment = appointmentData["appointments"][i];
        if(appointment["day"]===day &&
            appointment["month"]===month &&
            appointment["year"]===year) {
                appointments.push(appointment);
            }
    }
    return appointments;
}

const months = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December"
];

})(jQuery);
