export interface Specialty {
  name: string;
  code: string;
  popularity: number;
  self: string;
}

export interface SpecialtyQuery {
  page?: number;
  pageSize?: number;
  sort?: 'popularity' | 'standard';
  order?: 'asc' | 'desc';
}
