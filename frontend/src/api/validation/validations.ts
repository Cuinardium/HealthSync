import { HealthInsurance } from "../health-insurance/HealthInsurance";

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
