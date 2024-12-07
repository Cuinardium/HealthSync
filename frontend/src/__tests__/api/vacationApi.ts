import { axios } from "../../api/axios";
import { VacationForm, VacationQuery } from "../../api/vacation/Vacation";
import {
  createVacation,
  deleteVacation,
  getVacation,
  getVacations,
} from "../../api/vacation/vacationApi";
import { parseLocalDate } from "../../api/util/dateUtils";

jest.mock("../../api/axios");

const mockedAxios = axios as jest.Mocked<typeof axios>;

describe("Vacation API Tests", () => {
  const doctorId = "123";
  const vacationId = "456";

  beforeEach(() => {
    jest.resetAllMocks();
  });

  it("should retrieve vacations for a doctor", async () => {
    const query: VacationQuery = { page: 1, pageSize: 10 };
    const mockResponse = {
      status: 200,
      data: [
        {
          id: "1",
          fromDate: "2024-12-10",
          fromTime: "08:00",
          toDate: "2024-12-15",
          toTime: "18:00",
        },
      ],
      headers: { link: 'rel="last" page=1' },
    };

    mockedAxios.get.mockResolvedValue(mockResponse);

    const vacationsPage = await getVacations(doctorId, query);

    expect(mockedAxios.get).toHaveBeenCalledWith(
      `/doctors/${doctorId}/vacations`,
      {
        params: query,
        headers: { Accept: "application/vnd.vacation-list.v1+json" },
      },
    );

    expect(vacationsPage.content).toHaveLength(1);
    const expectedDate = parseLocalDate("2024-12-10");
    expect(vacationsPage.content[0].fromDate).toStrictEqual(expectedDate);
    const expectedToDate = parseLocalDate("2024-12-15");
    expect(vacationsPage.content[0].toDate).toStrictEqual(expectedToDate);
    expect(vacationsPage.content[0].id).toBe("1");
    expect(vacationsPage.content[0].fromTime).toBe("08:00");
    expect(vacationsPage.content[0].toTime).toBe("18:00");
  });

  it("should create a new vacation for a doctor", async () => {
    const vacationForm: VacationForm = {
      fromDate: parseLocalDate("2024-12-10"),
      fromTime: "08:00",
      toDate: parseLocalDate("2024-12-15"),
      toTime: "18:00",
      cancelReason: "Personal reasons",
      cancelAppointments: true,
    };

    const mockPostResponse = {
      headers: { location: `/doctors/${doctorId}/vacations/${vacationId}` },
    };
    const mockGetResponse = {
      data: {
        id: "456",
        fromDate: "2024-12-10",
        fromTime: "08:00",
        toDate: "2024-12-15",
        toTime: "18:00",
      },
    };

    mockedAxios.post.mockResolvedValue(mockPostResponse);
    mockedAxios.get.mockResolvedValue(mockGetResponse);

    const vacation = await createVacation(doctorId, vacationForm);

    expect(mockedAxios.post).toHaveBeenCalledWith(
      `/doctors/${doctorId}/vacations`,
      {
        ...vacationForm,
        fromDate: "2024-12-10",
        toDate: "2024-12-15",
      },
      { headers: { "Content-Type": "application/vnd.vacation-form.v1+json" } },
    );

    expect(mockedAxios.get).toHaveBeenCalledWith(
      `/doctors/${doctorId}/vacations/${vacationId}`,
      { headers: { Accept: "application/vnd.vacation.v1+json" } },
    );

    expect(vacation.id).toBe("456");
  });

  it("should retrieve a single vacation by ID", async () => {
    const mockResponse = {
      data: {
        id: "456",
        fromDate: "2024-12-10",
        fromTime: "08:00",
        toDate: "2024-12-15",
        toTime: "18:00",
      },
    };

    mockedAxios.get.mockResolvedValue(mockResponse);

    const vacation = await getVacation(doctorId, vacationId);

    expect(mockedAxios.get).toHaveBeenCalledWith(
      `/doctors/${doctorId}/vacations/${vacationId}`,
      { headers: { Accept: "application/vnd.vacation.v1+json" } },
    );

    expect(vacation.id).toBe("456");
    const expectedDate = parseLocalDate("2024-12-10");
    expect(vacation.fromDate).toStrictEqual(expectedDate);
    const expectedToDate = parseLocalDate("2024-12-15");
    expect(vacation.toDate).toStrictEqual(expectedToDate);
  });

  it("should delete a vacation by ID", async () => {
    mockedAxios.delete.mockResolvedValue({ status: 204 });

    await deleteVacation(doctorId, vacationId);

    expect(mockedAxios.delete).toHaveBeenCalledWith(
      `/doctors/${doctorId}/vacations/${vacationId}`,
    );
  });
});
