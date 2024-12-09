import { HealthInsurance } from "../health-insurance/HealthInsurance";
import { Specialty } from "../specialty/Specialty";
import { LOCALES } from "../locale/locale";
import { VacationForm } from "../vacation/Vacation";
import { Time, TIMES } from "../time/Time";
import {parseLocalDate} from "../util/dateUtils";

export function validateName(name: string | null): string | boolean {
  const validationMessages = {
    required: "validation.name.required",
    size: "validation.name.size",
    pattern: "validation.name.pattern",
  };

  // Check for null or empty string
  if (name === null || name.trim() === "") {
    return validationMessages.required;
  }

  // Check size constraints (1 to 50 characters)
  if (name.length < 1 || name.length > 50) {
    return validationMessages.size;
  }

  // Check pattern (only allows letters, spaces, and accented characters)
  const pattern = /^[a-zA-Z ñÑáÁéÉíÍóÓúÚ]+$/;
  if (!pattern.test(name)) {
    return validationMessages.pattern;
  }

  return true;
}

export function validateLastname(lastname: string | null): string | true {
  if (lastname === null || lastname.trim() === "") {
    return "validation.lastname.required";
  }
  if (lastname.length < 1 || lastname.length > 50) {
    return "validation.lastname.size";
  }
  const pattern = /^[a-zA-Z ñÑáÁéÉíÍóÓúÚ]+$/;
  if (!pattern.test(lastname)) {
    return "validation.lastname.pattern";
  }
  return true;
}

export function validateEmail(email: string | null): string | true {
  if (email === null || email.trim() === "") {
    return "validation.email.required";
  }
  if (email.length < 1 || email.length > 50) {
    return "validation.email.size";
  }
  const pattern = /^[a-zA-Z0-9.+-ñÑ]+@[a-zA-Z0-9.-]+(.com|.com.ar|.edu.ar)$/;
  if (!pattern.test(email)) {
    return "validation.email.pattern";
  }
  return true;
}

export function validatePassword(password: string | null): string | true {
  if (password === null || password.trim() === "") {
    return "validation.password.required";
  }
  if (password.length < 4 || password.length > 50) {
    return "validation.password.size";
  }
  const pattern = /^[a-zA-Z0-9]+$/;
  if (!pattern.test(password)) {
    return "validation.password.pattern";
  }
  return true;
}

export function validateConfirmPassword(
  confirmPassword: string | null,
): string | true {
  if (confirmPassword === null || confirmPassword.trim() === "") {
    return "validation.confirmPassword.required";
  }
  return true; // Validation passes
}

export function validateHealthInsurance(
  healthInsurance: string | null,
  healthInsurances: HealthInsurance[] | undefined,
): string | true {
  if (healthInsurance === null || healthInsurance.trim() === "") {
    return "validation.healthInsurance.required";
  }
  if (!healthInsurances?.some((hi) => hi.code === healthInsurance)) {
    return "validation.healthInsurance.invalid";
  }
  return true;
}

export function validateHealthInsurances(
  healthInsurancesField: string[] | null,
  healthInsurances: HealthInsurance[] | undefined,
): string | true {
  if (!healthInsurancesField || healthInsurancesField.length === 0) {
    return "validation.healthInsurances.required";
  }
  for (const insurance of healthInsurancesField) {
    if (!healthInsurances?.some((hi) => hi.code === insurance)) {
      return "validation.healthInsurances.invalid";
    }
  }
  return true;
}

export function validateCity(city: string | null): string | true {
  if (!city || city.trim() === "") {
    return "validation.city.required";
  }
  if (city.length < 1 || city.length > 50) {
    return "validation.city.invalidLength";
  }
  if (!/^[a-zA-Z0-9. ñÑáÁéÉíÍóÓúÚ]+$/.test(city)) {
    return "validation.city.invalidFormat";
  }
  return true;
}

export function validateAddress(address: string | null): string | true {
  if (!address || address.trim() === "") {
    return "validation.address.required";
  }
  if (address.length < 1 || address.length > 100) {
    return "validation.address.invalidLength";
  }
  if (!/^[a-zA-Z0-9. ñÑáÁéÉíÍóÓúÚ]+$/.test(address)) {
    return "validation.address.invalidFormat";
  }
  return true;
}

export function validateSpecialty(
  specialty: string | null,
  specialties: Specialty[] | undefined,
): string | true {
  if (!specialty || specialty.trim() === "") {
    return "validation.specialty.required";
  }
  if (!specialties?.some((sp) => sp.code === specialty)) {
    return "validation.specialty.invalid";
  }
  return true;
}

export function validateLocale(locale: string | null): string | true {
  if (!locale || locale.trim() === "") {
    return "validation.locale.required";
  }
  if (!LOCALES.includes(locale as any)) {
    return "validation.locale.invalid";
  }
  return true;
}

export function validateImage(file: File | undefined): string | true {
  if (!file) {
    return true;
  }

  const MAX_IMAGE_SIZE = 2 * 1024 * 1024; // 2MB

  const SUPPORTED_TYPES = ["png", "jpg", "jpeg"];

  // Validate file type
  const fileExtension = file.name.split(".").pop()?.toLowerCase();
  if (fileExtension && !SUPPORTED_TYPES.includes(fileExtension.toLowerCase())) {
    return "validation.image.type";
  }

  // Validate file size
  if (file.size > MAX_IMAGE_SIZE) {
    return "validation.image.size";
  }

  return true;
}

export function validateVacation(vacation: VacationForm): string | true {
  const fromDate = vacation.fromDate;
  const toDate = vacation.toDate;

  const fromTime = TIMES.indexOf(vacation.fromTime as Time);
  const toTime = TIMES.indexOf(vacation.toTime as Time);

  if (fromDate > toDate) {
    return "validation.vacation.invalid";
  }

  if (fromDate === toDate && fromTime >= toTime) {
    return "validation.vacation.invalid";
  }

  return true;
}

export function validateVacationDate(
  date: Date | string | null | undefined,
): string | true {
  if (!date) {
    return "validation.vacation.date.required";
  }

  date = typeof date === "string" ? parseLocalDate(date) : date;

  const today = new Date();
  today.setHours(0, 0, 0, 0);

  if (date < today) {
    return "validation.vacation.date.invalid";
  }

  return true;
}

export function validateVacationTime(
  time: string | null | undefined,
  fromDate: Date | string | null | undefined,
): string | true {
  fromDate = typeof fromDate === "string" ? parseLocalDate(fromDate) : fromDate;
  if (!time) {
    return "validation.vacation.time.required";
  }

  if (!TIMES.includes(time as Time)) {
    return "validation.vacation.time.notInEnum";
  }

  if (fromDate) {
    const today = new Date();

    // Times are blocks of 30 minutes, so we need to check if the time is in the past
    const currentTime =
      today.getHours() * 2 + Math.floor(today.getMinutes() / 30);

    const selectedTime = TIMES.indexOf(time as Time);

    if (fromDate.getDate() === today.getDate() && selectedTime <= currentTime) {
      return "validation.vacation.time.invalid";
    }
  }

  return true;
}

export function validateCancelDescription(
  description: string | undefined,
): string | true {
  if (!description || description.trim() === "") {
    return "validation.cancelDescription.required";
  }

  if (description.length < 1 || description.length > 1000) {
    return "validation.cancelDescription.size";
  }

  return true;
}

export function validateIndications(indications: string | null): string | boolean {
  const validationMessages = {
    required: "validation.indications.required",
    size: "validation.indications.size",
  };

  // Check for null or empty string
  if (indications === null || indications.trim() === "") {
    return validationMessages.required;
  }

  // Check size constraints (1 to 1000 characters)
  if (indications.length < 1 || indications.length > 1000) {
    return validationMessages.size;
  }

  return true;
}

export function validateFile(file: File | undefined): string | true {
  if (!file) {
    return true;
  }

  const SUPPORTED_TYPES = ["png", "jpg", "jpeg", "pdf"];
  const MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB (adjust as needed)

  // Validate file type
  const fileExtension = file.name.split(".").pop()?.toLowerCase();
  if (fileExtension && !SUPPORTED_TYPES.includes(fileExtension.toLowerCase())) {
    return "validation.file.type";
  }

  // Validate file size
  if (file.size > MAX_FILE_SIZE) {
    return "validation.file.size";
  }

  return true;
}

export function validateReviewDescription(
  description: string | null | undefined,
): string | true {
  if (!description || description.trim() === "") {
    return "validation.review.description.required";
  }

  if (description.length < 1 || description.length > 1000) {
    return "validation.review.description.size";
  }

  return true;
}

export function validateReviewRating(rating: number | null): string | true {
  if (!rating) {
    return "validation.review.rating.required";
  }

  if (rating < 1 || rating > 5) {
    return "validation.review.rating.invalid";
  }

  return true;
}

export function validateAppointmentDescription(
    description: string | null | undefined,
): string | true {
  if (!description || description.trim() === "") {
    return "validation.appointment.description.required";
  }

  if (description.length < 1 || description.length > 1000) {
    return "validation.appointment.description.size";
  }

  return true;
}

