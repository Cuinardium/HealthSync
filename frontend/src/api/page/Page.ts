import { AxiosResponse } from "axios";
import { axios } from "../axios";

export interface Page<T> {
  content: T[];
  totalPages: number;
  currentPage: number;
}

export function getPage<T>(response: AxiosResponse): Page<T> {

  // Last Page URL, RFC 5988
  const linkHeader = response.headers.link;
  const lastPageUrl = linkHeader?.split(", ").find((link: string) => link.includes('rel="last"'));
  const lastPage = Number(lastPageUrl?.match(/page=(\d+)/)?.[1]);
  
  const nextPageUrl = linkHeader?.split(", ").find((link: string) => link.includes('rel="next"'));
  const nextPage = nextPageUrl?.match(/page=(\d+)/)?.[1];

  const currentPage = nextPage ? Number(nextPage) - 1 : lastPage;

  if (response.status !== 200) {
    return {
      content: [],
      totalPages: 0,
      currentPage: 0,
    };
  }

  return {
    content: response.data,
    totalPages: lastPage,
    currentPage: currentPage,
  };
}

export async function fetchAllPaginatedData<T>(
    endpoint: string,
    query: Record<string, any>,
    headers: Record<string, string>,
): Promise<T[]> {
  const allData: T[] = [];
  let nextPageUrl: string | null = endpoint;
  let isFirstRequest = true;

  while (nextPageUrl) {
    const response = await axios.get<T[]>(nextPageUrl, {
      params: isFirstRequest ? query : {},
      headers,
    });

    isFirstRequest = false;

    if (!response.data || response.status !== 200) {
      break;
    }

    allData.push(...response.data);

    const linkHeader: string = response.headers.link;

    // Parse RFC 5988 Link header
    nextPageUrl = linkHeader?.match(/<([^>]+)>;\s*rel="next"/)?.[1] || null;
  }

  return allData;
}
