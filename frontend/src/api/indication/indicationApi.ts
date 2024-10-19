import { axios } from "../axios";
import { Indication, IndicationForm, IndicationQuery } from "./Indication";

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
): Promise<Indication[]> {
  const response = await axios.get<Indication[]>(
    INDICATION_ENDPOINT(appointmentId),
    {
      params: query,
      headers: { Accept: INDICATION_LIST_CONTENT_TYPE },
    },
  );
  return response.data;
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

  const response =await axios.post(INDICATION_ENDPOINT(appointmentId), formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });

  const location = response.headers.location;
  const indicationId = location.split("/").pop();

  return await getIndication(appointmentId, indicationId as string);
}

// =========== indications/id ==============

export async function getIndication(appointmentId: string, id: string): Promise<Indication> {
  const response = await axios.get<Indication>(
    `${INDICATION_ENDPOINT(appointmentId)}/${id}`,
    {
      headers: { Accept: INDICATION_CONTENT_TYPE },
    },
  );
  return response.data;
}

// ============ files/id ==============

export async function getFile(appointmentId: string, id: string): Promise<Blob> {
  const response = await axios.get(`${FILE_ENDPOINT(appointmentId)}/${id}`, {
    responseType: "blob",
  });
  return response.data;
}
