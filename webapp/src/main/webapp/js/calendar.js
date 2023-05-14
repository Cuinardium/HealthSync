(function($) {

	"use strict";

	// Setup the calendar with the current date
$(document).ready(function(){
    var date = new Date();
    var today = date.getDate();
    // Set click handlers for DOM elements
    $(".right-button").click({date: date}, next_year);
    $(".left-button").click({date: date}, prev_year);
    $(".month").click({date: date}, month_click);
    $("#add-button").click({date: date}, new_appointment);
    // Set current month as active
    $(".months-row").children().eq(date.getMonth()).addClass("active-month");
    init_calendar(date);
    var appointments = check_appointments(today, date.getMonth()+1, date.getFullYear());
    show_appointments(appointments, months[date.getMonth()], today);
});

// Initialize the calendar by appending the HTML dates
function init_calendar(date) {
    $(".tbody").empty();
    $(".appointments-container").empty();
    var calendar_days = $(".tbody");
    var month = date.getMonth();
    var year = date.getFullYear();
    var day_count = days_in_month(month, year);
    var row = $("<tr class='table-row'></tr>");
    var today = date.getDate();
    // Set date to 1 to find the first day of the month
    date.setDate(1);
    var first_day = date.getDay();
    // 35+firstDay is the number of date elements to be added to the dates table
    // 35 is from (7 days in a week) * (up to 5 rows of dates in a month)
    for(var i=0; i<35+first_day; i++) {
        // Since some of the elements will be blank,
        // need to calculate actual date from index
        var day = i-first_day+1;
        // If it is a sunday, make a new row
        if(i%7===0) {
            calendar_days.append(row);
            row = $("<tr class='table-row'></tr>");
        }
        // if current index isn't a day in this month, make it blank
        if(i < first_day || day > day_count) {
            var curr_date = $("<td class='table-date nil'>"+"</td>");
            row.append(curr_date);
        }
        else {
            var curr_date = $("<td class='table-date'>"+day+"</td>");
            var appointments = check_appointments(day, month+1, year);
            if(today===day && $(".active-date").length===0) {
                curr_date.addClass("active-date");
                show_appointments(appointments, months[month], day);
            }
            // If this date has any appointments, style it with .appointment-date
            if(appointments.length!==0) {
                curr_date.addClass("appointment-date");
            }
            // Set onClick handler for clicking a date
            curr_date.click({appointments: appointments, month: months[month], day:day}, date_click);
            row.append(curr_date);
        }
    }
    // Append the last row and set the current year
    calendar_days.append(row);
    $(".year").text(year);
}

// Get the number of days in a given month/year
function days_in_month(month, year) {
    var monthStart = new Date(year, month, 1);
    var monthEnd = new Date(year, month + 1, 1);
    return (monthEnd - monthStart) / (1000 * 60 * 60 * 24);
}

// appointment handler for when a date is clicked
function date_click(appointment) {
    $(".appointments-container").show(250);
    $("#dialog").hide(250);
    $(".active-date").removeClass("active-date");
    $(this).addClass("active-date");
    show_appointments(appointment.data.appointments, appointment.data.month, appointment.data.day);
};

// Appointment handler for when a month is clicked
function month_click(appointment){
    $(".appointments-container").show(250);
    $("#dialog").hide(250);
    var date = appointment.data.date;
    $(".active-month").removeClass("active-month");
    $(this).addClass("active-month");
    var new_month = $(".month").index(this);
    date.setMonth(new_month);
    init_calendar(date);
}

// appointment handler for when the year right-button is clicked
function next_year(appointment) {
    $("#dialog").hide(250);
    var date = appointment.data.date;
    var new_year = date.getFullYear()+1;
    $("year").html(new_year);
    date.setFullYear(new_year);
    init_calendar(date);
}

// appointment handler for when the year left-button is clicked
function prev_year(appointment) {
    $("#dialog").hide(250);
    var date = appointment.data.date;
    var new_year = date.getFullYear()-1;
    $("year").html(new_year);
    date.setFullYear(new_year);
    init_calendar(date);
}

// appointment handler for clicking the new appointment button
function new_appointment(appointment) {
    // if a date isn't selected then do nothing
    if($(".active-date").length===0)
        return;
    // remove red error input on click
    $("input").click(function(){
        $(this).removeClass("error-input");
    })
    // empty inputs and hide appointments
    $("#dialog input[type=text]").val('');
    $("#dialog input[type=number]").val('');

    // appointment handler for ok button
    $("#ok-button").unbind().click({date: appointment.data.date}, function() {
        var date = appointment.data.date;
        var time = $("#time").val().trim();
        var day = parseInt($(".active-date").html());
        // Basic form validation
        if(time.length === 0) {
            $("#time").addClass("error-input");
        }
        else {
            $("#dialog").hide(250);
            console.log("new appointment");
            new_appointment_json(time, date);
            date.setDate(day);
            init_calendar(date);
        }
    });
}

// Adds a json event to appointment_data
    function new_appointment_json(time, date) {
        var event = {
            "time": time,
            "year": date.getFullYear(),
            "month": date.getMonth()+1,
            "day": date.getDay(),
        };
        appointment_data["events"].push(event);
    }

// Display all appointments of the selected date in card views
function show_appointments(appointments, month, day) {
    // Clear the dates container
    $(".appointments-container").empty();
    $(".appointments-container").show(250);
    console.log(appointment_data["appointments"]);
    // If there are no appointments for this date, notify the user
    if(appointments.length===0) {
        var appointment_card = $("<div class='card noAppointments'></div>");
        var appointment_time = $("<div class='appointment-time'>There are no appointments available for " +month+" "+day+"</div>");
        $(appointment_card).append(appointment_time);
        $(".appointments-container").append(appointment_card);
    }
    else {
        // Go through and add each appointment as a card to the appointments container
        for(var i=0; i<appointments.length; i++) {
            var appointment_card = $("<div class='card appointment-card'></div>");
            var appointment_time = $("<div class='appointment-time'>"+appointments[i]["time"]+"</div>");
            $(appointment_card).append(appointment_time);
            $(".appointments-container").append(appointment_card);
        }
    }
}

// Checks if a specific date has any appointments
function check_appointments(day, month, year) {
    var appointments = [];
    for(var i=0; i<appointment_data["appointments"].length; i++) {
        var appointment = appointment_data["appointments"][i];
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
