import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import ScheduleSelector from "../../components/doctors/ScheduleSelector";
import { useAttendingHours } from "../../hooks/doctorHooks";

jest.mock("../../hooks/doctorHooks", () => ({
  useAttendingHours: jest.fn(),
}));

jest.mock("i18next", () => ({
  t: (key: string) => key, // Mock translation function
}));

const mockAttendingHours = [
  { day: "Monday", hours: ["08:00", "09:00"] },
  { day: "Wednesday", hours: ["10:00", "11:00"] },
];

describe("ScheduleSelector Component", () => {
  const mockOnScheduleChange = jest.fn();

  beforeEach(() => {
    jest.clearAllMocks();
    (useAttendingHours as jest.Mock).mockReturnValue({
      data: mockAttendingHours,
    });
  });

  it("renders the component with the correct initial schedule", () => {
    render(
      <ScheduleSelector
        doctorId="123"
        onScheduleChange={mockOnScheduleChange}
      />,
    );

    // Check that the schedule is displayed for predefined attending hours
    expect(screen.getAllByText("day.monday")).toHaveLength(1);
    expect(screen.getAllByText("day.wednesday")).toHaveLength(1);
  });

  it("updates the schedule when a time slot is selected", () => {
    render(
      <ScheduleSelector
        doctorId="123"
        onScheduleChange={mockOnScheduleChange}
      />,
    );

    const mondayTimeBlock = screen.getAllByText("08:00 - 08:30")[0]; // Find the time block
    fireEvent.mouseDown(mondayTimeBlock); // Simulate selecting the block

    expect(mockOnScheduleChange).toHaveBeenCalledWith([
      { day: "Monday", hours: ["08:00", "09:00"] },
      { day: "Wednesday", hours: ["10:00", "11:00"] },
    ]);
  });

  it("toggles weekend visibility", () => {
    render(
      <ScheduleSelector
        doctorId="123"
        onScheduleChange={mockOnScheduleChange}
      />,
    );

    const weekendSwitch = screen.getByLabelText("changeSchedule.showWeekend");

    expect(screen.queryByText("day.saturday")).toBeNull(); // Verify weekends are hidden

    fireEvent.click(weekendSwitch);

    expect(screen.getByText("day.saturday")).toBeInTheDocument(); // Verify weekends are now visible
  });

  it("handles no initial data gracefully", () => {
    (useAttendingHours as jest.Mock).mockReturnValue({ data: [] });

    render(
      <ScheduleSelector
        doctorId="123"
        onScheduleChange={mockOnScheduleChange}
      />,
    );

    expect(screen.getByText("day.monday")).toBeInTheDocument(); // Default rendering
  });
});
