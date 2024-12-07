import { axios } from "../../api/axios";
import {
  createIndication,
  getFile,
  getIndication,
  getIndications,
  mapDetails,
} from "../../api/indication/indicationApi";
import { IndicationResponse } from "../../api/indication/Indication";

const mockedAxios = axios as jest.Mocked<typeof axios>;

jest.mock("../../api/axios");

describe("Indication API Tests", () => {
  const appointmentId = "123";
  const indicationId = "456";
  const fileId = "789";

  beforeEach(() => {
    jest.resetAllMocks();
  });

  it("should fetch a paginated list of indications", async () => {
    const mockResponse = {
      data: [
        {
          id: 1,
          date: "2024-12-01",
          description: "Test indication 1",
          links: [
            { rel: "creator", href: "/users/1" },
            { rel: "file", href: "/files/1" },
          ],
        },
        {
          id: 2,
          date: "2024-12-02",
          description: "Test indication 2",
          links: [
            { rel: "creator", href: "/users/2" },
            { rel: "file", href: "/files/2" },
          ],
        },
      ],
      status: 200,
      headers: {
        link: 'rel="last" page=1',
      },
    };

    mockedAxios.get.mockResolvedValue(mockResponse);

    const query = { page: 1, pageSize: 10 };
    const page = await getIndications(appointmentId, query);

    expect(page.content).toHaveLength(2);
    expect(page.totalPages).toBe(1);
    expect(page.currentPage).toBe(1);
    expect(page.content[0].id).toBe(1);
    expect(page.content[1].id).toBe(2);
  });

  it("should create a new indication", async () => {
    const indicationData = {
      indications: "Test indication for appointment",
      file: new File(["test"], "testfile.pdf"),
    };

    const mockPostResponse = {
      headers: { location: "/appointments/123/indications/456" },
    };

    mockedAxios.post.mockResolvedValue(mockPostResponse);
    mockedAxios.get.mockResolvedValue({
      data: {
        id: 456,
        date: "2024-12-02",
        description: "Test indication for appointment",
        links: [
          { rel: "creator", href: "/users/1" },
          { rel: "file", href: "/files/789" },
        ],
      },
    });

    const indication = await createIndication(appointmentId, indicationData);

    expect(indication.id).toBe(456);
    expect(indication.description).toBe("Test indication for appointment");
  });

  it("should fetch an indication by id", async () => {
    const mockResponse = {
      data: {
        id: 456,
        date: "2024-12-02",
        description: "Test indication for appointment",
        links: [
          { rel: "creator", href: "/users/1" },
          { rel: "file", href: "/files/789" },
        ],
      },
    };

    mockedAxios.get.mockResolvedValue(mockResponse);

    const indication = await getIndication(appointmentId, indicationId);

    expect(indication.id).toBe(456);
    expect(indication.description).toBe("Test indication for appointment");
  });

  it("should fetch a file for an indication", async () => {
    const mockResponse = {
      data: new Blob(["test file content"], { type: "application/pdf" }),
      headers: { "content-disposition": 'attachment; filename="testfile.pdf"' },
    };

    mockedAxios.get.mockResolvedValue(mockResponse);

    const file = await getFile(appointmentId, fileId);

    expect(file.fileName).toBe("testfile.pdf");
    expect(file.blob).toBeInstanceOf(Blob);
  });

  it("should map indication response data to model", () => {
    const indicationResponse: IndicationResponse = {
      id: 456,
      date: "2024-12-02",
      description: "Test indication for appointment",
      links: [
        { rel: "creator", href: "/users/1", method: "GET" },
        { rel: "file", href: "/files/789", method: "GET" },
      ],
    };

    const mappedIndication = mapDetails(indicationResponse);

    expect(mappedIndication.id).toBe(456);
    expect(mappedIndication.description).toBe(
      "Test indication for appointment",
    );
    expect(mappedIndication.creatorId).toBe("1");
    expect(mappedIndication.fileId).toBe("789");
  });
});
