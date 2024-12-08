import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import { useDoctorQueryContext } from "../../context/DoctorQueryContext";
import { useHealthInsurances } from "../../hooks/healthInsuranceHooks";
import { useSpecialties } from "../../hooks/specialtyHooks";
import { useCities } from "../../hooks/cityHooks";
import DoctorFilters from "../../components/doctors/DoctorFilters";

jest.mock("../../context/DoctorQueryContext", () => ({
  useDoctorQueryContext: jest.fn(),
}));

jest.mock("../../hooks/healthInsuranceHooks", () => ({
  useHealthInsurances: jest.fn(),
}));

jest.mock("../../hooks/specialtyHooks", () => ({
  useSpecialties: jest.fn(),
}));

jest.mock("../../hooks/cityHooks", () => ({
  useCities: jest.fn(),
}));

jest.mock("react-i18next", () => ({
  useTranslation: () => ({
    t: (key: string) => key, // Mock translation function
  }),
}));

const mockHealthInsurances = [
  { code: "insurance_1", popularity: 50 },
  { code: "insurance_2", popularity: 30 },
];

const mockSpecialties = [
  { code: "specialty_1", popularity: 70 },
  { code: "specialty_2", popularity: 40 },
];

const mockCities = [
  { name: "City 1", popularity: 100 },
  { name: "City 2", popularity: 80 },
];

const mockQueryContext = {
  query: {
    city: ["City 1"],
    healthInsurance: ["insurance_1"],
    specialty: ["specialty_1"],
    minRating: 3,
    date: new Date(),
    fromTime: "08:00",
    toTime: "12:00",
  },
  setCity: jest.fn(),
  setSpecialty: jest.fn(),
  setHealthInsurance: jest.fn(),
  setMinRating: jest.fn(),
  setDate: jest.fn(),
  setFromTime: jest.fn(),
  setToTime: jest.fn(),
  resetQuery: jest.fn(),
};

describe("DoctorFilters Component", () => {
  beforeEach(() => {
    jest.clearAllMocks();

    (useDoctorQueryContext as jest.Mock).mockReturnValue(mockQueryContext);
    (useHealthInsurances as jest.Mock).mockReturnValue({
      data: mockHealthInsurances,
    });
    (useSpecialties as jest.Mock).mockReturnValue({
      data: mockSpecialties,
    });
    (useCities as jest.Mock).mockReturnValue({
      data: mockCities,
    });
  });

  it("renders the filters correctly", () => {
    render(<DoctorFilters />);

    expect(screen.getByText("filters.title")).toBeInTheDocument();
    expect(screen.getByText("form.city")).toBeInTheDocument();
    expect(screen.getByText("form.healthcare")).toBeInTheDocument();
    expect(screen.getByText("form.specialization")).toBeInTheDocument();
  });

  it("resets all filters on reset", () => {
    render(<DoctorFilters />);

    const resetButton = screen.getByTestId("resetButton");
    fireEvent.click(resetButton);

    expect(mockQueryContext.resetQuery).toHaveBeenCalled();
  });

  it("shows an error when a past date is selected", () => {
    render(<DoctorFilters />);

    const dateInput = screen.getByLabelText("vacation.date");
    fireEvent.change(dateInput, {
      target: { value: "2020-01-01" },
    });

    expect(screen.getByText("filters.pastDate")).toBeInTheDocument();
    expect(mockQueryContext.setDate).toHaveBeenCalledWith(undefined);
  });
});
