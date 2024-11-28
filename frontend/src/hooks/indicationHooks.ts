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
import { Page } from "../api/page/Page";
import { AxiosError } from "axios";

const STALE_TIME = 5 * 60 * 1000;

// To add the file to the indication
export interface IndicationWithFileData extends Indication {
  fileData?: IndicationFile;
}

// ========== useIndications ==========

export function useIndications(appointmentId: string, query: IndicationQuery) {
  return useQuery<Page<IndicationWithFileData>, AxiosError>(
    {
      queryKey: ["indications", appointmentId, query.page],
      queryFn: async () => {
        const indications = await getIndications(appointmentId, query);

        if (!indications) {
          return indications;
        }

        // Fetch the file data for each indication that has a file URL
        const indicationsWithFiles = await Promise.all(
          indications.content.map(async (indication) => {
            if (indication.fileId) {
              const fileData = await getFile(appointmentId, indication.fileId);
              return {
                ...indication,
                fileData,
              };
            }
            return indication;
          }),
        );

        const page = {
          ...indications,
          content: indicationsWithFiles,
        };

        return page;
      },
      enabled: !!appointmentId,
      staleTime: STALE_TIME,
    },
    queryClient,
  );
}

// ========== useIndication ==========

export function useIndication(appointmentId: string, indicationId: string) {
  return useQuery<Indication, AxiosError>(
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

export function useCreateIndication(
  appointmentId: string,
  onSuccess: () => void,
  onError: (error: AxiosError) => void,
) {
  return useMutation<Indication, AxiosError, IndicationForm>(
    {
      mutationFn: (indication: IndicationForm) =>
        createIndication(appointmentId, indication),
      onSuccess: () => {
        queryClient.invalidateQueries({
          queryKey: ["indications", appointmentId],
        });

        onSuccess();
      },
      onError: (error) => {
        onError(error);
      },
    },
    queryClient,
  );
}

// ========== useGetFile ==========

export function useGetFile(appointmentId: string, fileId: string) {
  return useQuery<IndicationFile, AxiosError>(
    {
      queryKey: ["file", appointmentId, fileId],
      queryFn: () => getFile(appointmentId, fileId),
      enabled: !!appointmentId && !!fileId,
    },
    queryClient,
  );
}
