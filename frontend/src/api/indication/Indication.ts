import { Link } from "../link/link";

export interface Indication {
  creator: string;
  date: Date;
  description: string;
  fileId?: string;
  id: number;
}

export interface IndicationForm {
  indications: string;
  file?: File;  
}

export interface IndicationQuery {
  page?: number;
  pageSize?: number;
}

export interface IndicationFile {
  blob: Blob;
  fileName: string;
}

export interface IndicationResponse {
  date: string;
  description: string;
  id: number;
  links: Link[];
}
