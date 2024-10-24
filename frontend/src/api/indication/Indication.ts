export interface Indication {
  appointment: string;
  creator: string;
  date: string;
  description: string;
  file: string;
  id: number;
  self: string;
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
