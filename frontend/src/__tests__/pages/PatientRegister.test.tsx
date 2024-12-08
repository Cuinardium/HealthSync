import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { MemoryRouter, useNavigate } from "react-router-dom";
import { useHealthInsurances } from "../../hooks/healthInsuranceHooks";
import { useCreatePatient } from "../../hooks/patientHooks";
import PatientRegister from "../../pages/auth/PatientRegister";
import { createPatient } from "../../api/patient/patientApi";

jest.mock("react-router-dom", () => ({
  ...jest.requireActual("react-router-dom"),
  useNavigate: jest.fn(),
}));

jest.mock("../../hooks/healthInsuranceHooks", () => ({
  useHealthInsurances: jest.fn(),
}));

jest.mock("../../hooks/patientHooks", () => ({
  useCreatePatient: jest.fn(),
}));

jest.mock("../../api/patient/patientApi", () => ({
  createPatient: jest.fn(),
}));

jest.mock("react-i18next", () => ({
  useTranslation: () => ({
    t: (key: string) => key, // Mock translation function
  }),
}));

describe("PatientRegister Component", () => {
  const mockNavigate = jest.fn();
  const mockHealthInsurances = [
    { code: "INSURANCE_1", name: "healthInsurance.insurance.1" },
    { code: "INSURANCE_2", name: "healthInsurance.insurance.2" },
  ];

  beforeEach(() => {
    jest.clearAllMocks();
    (useNavigate as jest.Mock).mockReturnValue(mockNavigate);
    (useHealthInsurances as jest.Mock).mockReturnValue({
      data: mockHealthInsurances,
    });
    (useCreatePatient as jest.Mock).mockImplementation(
      (onSuccess, onError) => ({
        mutate: async (data: any) => {
          try {
            await createPatient(data);
            onSuccess();
          } catch (error) {
            onError(error);
          }
        },
        isPending: false,
      }),
    );
  });

  it("renders the form correctly", () => {
    render(
      <MemoryRouter>
        <PatientRegister />
      </MemoryRouter>,
    );

    expect(screen.getByText("registerPatient.title")).toBeInTheDocument();
    expect(screen.getByText("form.name")).toBeInTheDocument();
    expect(screen.getByText("form.lastname")).toBeInTheDocument();
    expect(screen.getByText("form.email")).toBeInTheDocument();
    expect(screen.getByText("form.password")).toBeInTheDocument();
    expect(screen.getByText("form.cpassword")).toBeInTheDocument();
    expect(screen.getByText("form.healthcare")).toBeInTheDocument();
  });

  it("loads and displays health insurance options", () => {
    render(
      <MemoryRouter>
        <PatientRegister />
      </MemoryRouter>,
    );

    const insuranceSelect = screen.getByLabelText("form.healthcare");
    expect(insuranceSelect).toBeInTheDocument();

    const options = screen.getAllByRole("option");
    expect(options).toHaveLength(mockHealthInsurances.length + 1); // Includes the hint option
    expect(options[1]).toHaveTextContent("healthInsurance.insurance.1");
    expect(options[2]).toHaveTextContent("healthInsurance.insurance.2");
  });

  it("shows validation errors for invalid input", async () => {
    render(
      <MemoryRouter>
        <PatientRegister />
      </MemoryRouter>,
    );

    const submitButton = screen.getByRole("button", {
      name: "register.submit",
    });
    fireEvent.click(submitButton);

    expect(
      await screen.findByText("validation.name.required"),
    ).toBeInTheDocument();
    expect(
      await screen.findByText("validation.lastname.required"),
    ).toBeInTheDocument();
    expect(
      await screen.findByText("validation.email.required"),
    ).toBeInTheDocument();
    expect(
      await screen.findByText("validation.password.required"),
    ).toBeInTheDocument();
    expect(
      await screen.findByText("validation.confirmPassword.required"),
    ).toBeInTheDocument();
    expect(
      await screen.findByText("validation.healthInsurance.required"),
    ).toBeInTheDocument();

    expect(screen.getByText("form.name")).toBeInTheDocument();
    expect(screen.getByText("form.email")).toBeInTheDocument();
  });

  it("submits the form with valid data", async () => {

    (createPatient as jest.Mock).mockImplementation(() => {
      return Promise.resolve();
    });

    render(
      <MemoryRouter>
        <PatientRegister />
      </MemoryRouter>,
    );

    fireEvent.input(screen.getByPlaceholderText("form.name_hint"), {
      target: { value: "John" },
    });
    fireEvent.input(screen.getByPlaceholderText("form.lastname_hint"), {
      target: { value: "Doe" },
    });
    fireEvent.input(screen.getByPlaceholderText("form.email_hint"), {
      target: { value: "john.doe@example.com" },
    });
    fireEvent.change(screen.getByLabelText("form.healthcare"), {
      target: { value: "INSURANCE_1" },
    });
    fireEvent.input(screen.getByPlaceholderText("form.password_hint"), {
      target: { value: "Password123" },
    });
    fireEvent.input(screen.getByPlaceholderText("form.cpassword_hint"), {
      target: { value: "Password123" },
    });

    fireEvent.click(screen.getByRole("button", { name: "register.submit" }));

    await waitFor(() => {
      expect(createPatient).toHaveBeenCalledWith({
        name: "John",
        lastname: "Doe",
        email: "john.doe@example.com",
        healthInsurance: "INSURANCE_1",
        password: "Password123",
        confirmPassword: "Password123",
      });
    });
  });

  it("handles server-side errors", async () => {
    // Mock `createPatient` to throw a 409 Conflict error
    (createPatient as jest.Mock).mockImplementation(() => {
      // eslint-disable-next-line no-throw-literal
      throw {
        response: { status: 409 },
      };
    });

    render(
      <MemoryRouter>
        <PatientRegister />
      </MemoryRouter>,
    );

    fireEvent.input(screen.getByPlaceholderText("form.name_hint"), {
      target: { value: "John" },
    });
    fireEvent.input(screen.getByPlaceholderText("form.lastname_hint"), {
      target: { value: "Doe" },
    });
    fireEvent.input(screen.getByPlaceholderText("form.email_hint"), {
      target: { value: "john.doe@example.com" },
    });
    fireEvent.change(screen.getByLabelText("form.healthcare"), {
      target: { value: "INSURANCE_1" },
    });
    fireEvent.input(screen.getByPlaceholderText("form.password_hint"), {
      target: { value: "Password123" },
    });
    fireEvent.input(screen.getByPlaceholderText("form.cpassword_hint"), {
      target: { value: "Password123" },
    });

    fireEvent.click(screen.getByRole("button", { name: "register.submit" }));

    await waitFor(() => {
      expect(screen.getByText("register.emailInUse")).toBeInTheDocument();
    });
  });
});
