import { useMutation, useQuery } from "@tanstack/react-query";
import {
  getIndications,
  getIndication,
  createIndication,
  getFile,
} from "../api/indication/indicationApi";
import {
  Indication,
  IndicationFile,
  IndicationForm,
  IndicationQuery,
} from "../api/indication/Indication";
import { queryClient } from "../api/queryClient";

const STALE_TIME = 5 * 60 * 1000;

// To add the file to the indication
export interface IndicationWithFileData extends Indication {
  fileData?: IndicationFile;
}

// ========== useIndications ==========

export function useIndications(appointmentId: string, query: IndicationQuery) {
  return useQuery<IndicationWithFileData[], Error>(
    {
      queryKey: ["indications", appointmentId, query.page],
      queryFn: async () => {
        const indications = await getIndications(appointmentId, query);

        // Fetch the file data for each indication that has a file URL
        const indicationsWithFiles = await Promise.all(
          indications.map(async (indication) => {
            if (indication.file) {
              const fileId = indication.file.split("/").pop() as string;
              const fileData = await getFile(appointmentId, fileId);
              return {
                ...indication,
                fileData,
              };
            }
            return indication;
          }),
        );

        return indicationsWithFiles;
      },
      enabled: !!appointmentId,
      staleTime: STALE_TIME,
    },
    queryClient,
  );
}

// ========== useIndication ==========

export function useIndication(appointmentId: string, indicationId: string) {
  return useQuery<Indication, Error>(
    {
      queryKey: ["indication", appointmentId, indicationId],
      queryFn: () => getIndication(appointmentId, indicationId),
      enabled: !!appointmentId && !!indicationId,
      staleTime: STALE_TIME,
    },
    queryClient,
  );
}

// ========== useCreateIndication ==========

export function useCreateIndication(appointmentId: string) {
  return useMutation<Indication, Error, IndicationForm>(
    {
      mutationFn: (indication: IndicationForm) =>
        createIndication(appointmentId, indication),
      onSuccess: () => {
        queryClient.invalidateQueries({
          queryKey: ["indications", appointmentId],
        });
      },
    },
    queryClient,
  );
}

// ========== useGetFile ==========

export function useGetFile(appointmentId: string, fileId: string) {
  return useQuery<IndicationFile, Error>(
    {
      queryKey: ["file", appointmentId, fileId],
      queryFn: () => getFile(appointmentId, fileId),
      enabled: !!appointmentId && !!fileId,
    },
    queryClient,
  );
}
