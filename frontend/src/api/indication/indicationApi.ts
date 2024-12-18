import { axios } from "../axios";
import { getPage, Page } from "../page/Page";
import {
  Indication,
  IndicationFile,
  IndicationForm,
  IndicationQuery,
  IndicationResponse,
} from "./Indication";

const INDICATION_ENDPOINT = (appointmentId: string) =>
  `/appointments/${appointmentId}/indications`;

const FILE_ENDPOINT = (appointmentId: string) =>
  `/appointments/${appointmentId}/files`;

const INDICATION_CONTENT_TYPE = "application/vnd.indication.v1+json";
const INDICATION_LIST_CONTENT_TYPE = "application/vnd.indication-list.v1+json";

// =========== indications ==============

export async function getIndications(
  appointmentId: string,
  query: IndicationQuery,
): Promise<Page<Indication>> {
  const response = await axios.get(INDICATION_ENDPOINT(appointmentId), {
    params: query,
    headers: { Accept: INDICATION_LIST_CONTENT_TYPE },
  });

  if (response.status === 200) {
    response.data = response.data?.map((indication: IndicationResponse) =>
      mapDetails(indication),
    );
  }

  return getPage(response);
}

export async function createIndication(
  appointmentId: string,
  indication: IndicationForm,
): Promise<Indication> {
  const formData = new FormData();

  formData.append("indications", indication.indications);

  if (indication.file) {
    formData.append("file", indication.file);
  }

  const response = await axios.post(
    INDICATION_ENDPOINT(appointmentId),
    formData,
    {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    },
  );

  const location = response.headers.location;
  const indicationId = location.split("/").pop();

  return await getIndication(appointmentId, indicationId as string);
}

// =========== indications/id ==============

export async function getIndication(
  appointmentId: string,
  id: string,
): Promise<Indication> {
  const response = await axios.get<IndicationResponse>(
    `${INDICATION_ENDPOINT(appointmentId)}/${id}`,
    {
      headers: { Accept: INDICATION_CONTENT_TYPE },
    },
  );
  return mapDetails(response.data);
}

// ============ files/id ==============

export async function getFile(
  appointmentId: string,
  id: string,
): Promise<IndicationFile> {
  const response = await axios.get(`${FILE_ENDPOINT(appointmentId)}/${id}`, {
    responseType: "blob",
  });

  const contentDisposition = response.headers["content-disposition"];
  let fileName;

  if (contentDisposition) {
    const fileNameMatch = contentDisposition.match(/filename="(.+)"/);

    if (fileNameMatch && fileNameMatch.length > 1) {
      fileName = fileNameMatch[1];
    }
  }

  return {
    blob: response.data,
    fileName: fileName,
  };
}

// ========= auxiliary functions =========

export function mapDetails(indicationResp: IndicationResponse): Indication {
  const date = new Date(indicationResp.date + "T00:00:00");

  return {
    creatorId: indicationResp.links.find((link) => link.rel === "creator")?.href.split("/").pop() as string,
    date: date,
    description: indicationResp.description,
    fileId: indicationResp.links.find((link) => link.rel === "file")?.href.split("/").pop() as string,
    id: indicationResp.id,
  };
}
