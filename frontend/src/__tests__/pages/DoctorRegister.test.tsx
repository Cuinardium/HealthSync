import React from "react";
import { render, screen} from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import { useSpecialties } from "../../hooks/specialtyHooks";
import { useHealthInsurances } from "../../hooks/healthInsuranceHooks";
import { useCreateDoctor } from "../../hooks/doctorHooks";
import DoctorRegister from "../../pages/auth/DoctorRegister";

jest.mock("../../hooks/specialtyHooks", () => ({
  useSpecialties: jest.fn(),
}));

jest.mock("../../hooks/healthInsuranceHooks", () => ({
  useHealthInsurances: jest.fn(),
}));

jest.mock("../../hooks/doctorHooks", () => ({
  useCreateDoctor: jest.fn(),
}));

jest.mock("../../components/doctors/PlaceAutocomplete", () => (props: any) => {
  const { onPlaceSelect } = props;
  return (
    <input
      aria-label="PlaceAutocomplete"
      onChange={(e) =>
        onPlaceSelect?.(e.target.value, e.target.value.split(",")[1])
      }
    />
  );
});

jest.mock(
  "../../components/doctors/HealthInsurancePicker",
  () => (props: any) => {
    return <div>MockHealthInsurancePicker</div>;
  },
);

jest.mock("react-i18next", () => ({
  useTranslation: () => ({
    t: (key: string) => key, // Mock translation function
  }),
}));

describe("DoctorRegister Component", () => {
  const mockSpecialties = [
    { code: "specialty_1", name: "Specialty 1" },
    { code: "specialty_2", name: "Specialty 2" },
  ];
  const mockHealthInsurances = [
    { code: "insurance_1", name: "Insurance 1" },
    { code: "insurance_2", name: "Insurance 2" },
  ];

  beforeEach(() => {
    jest.clearAllMocks();

    // Mock dependencies
    (useSpecialties as jest.Mock).mockReturnValue({
      data: mockSpecialties,
    });

    (useHealthInsurances as jest.Mock).mockReturnValue({
      data: mockHealthInsurances,
    });

    (useCreateDoctor as jest.Mock).mockImplementation((onSuccess, onError) => ({
      mutate: (data: any) => {
        if (data.email === "error@example.com") {
          onError({ response: { status: 409 } });
        } else {
          onSuccess();
        }
      },
      isPending: false,
    }));
  });

  it("renders the form correctly", () => {
    render(
      <MemoryRouter>
        <DoctorRegister />
      </MemoryRouter>,
    );

    expect(screen.getByText("registerMedic.title")).toBeInTheDocument();
    expect(screen.getByLabelText("form.name")).toBeInTheDocument();
    expect(screen.getByLabelText("form.lastname")).toBeInTheDocument();
    expect(screen.getByLabelText("form.email")).toBeInTheDocument();
    expect(screen.getByLabelText("form.password")).toBeInTheDocument();
    expect(screen.getByLabelText("form.cpassword")).toBeInTheDocument();
    expect(screen.getByLabelText("PlaceAutocomplete")).toBeInTheDocument();
    expect(screen.getByText("MockHealthInsurancePicker")).toBeInTheDocument();
  });


  it("displays specialties correctly", () => {
    render(
      <MemoryRouter>
        <DoctorRegister />
      </MemoryRouter>,
    );

    const specialtySelect = screen.getByLabelText("form.specialization");
    expect(specialtySelect).toBeInTheDocument();

    const options = screen.getAllByRole("option");
    expect(options).toHaveLength(mockSpecialties.length + 1); // Includes the hint option
    expect(options[1]).toHaveTextContent("specialty.specialty.1");
    expect(options[2]).toHaveTextContent("specialty.specialty.2");
  });
});
