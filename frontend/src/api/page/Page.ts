import { AxiosResponse } from "axios";

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

  return {
    content: response.data,
    totalPages: lastPage,
    currentPage: currentPage,
  };
}
