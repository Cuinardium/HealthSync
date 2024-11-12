export interface Specialty {
  name: string;
  code: string;
  popularity: number;
  self: string;
}

export interface SpecialtyQuery {
  sort?: 'popularity' | 'standard';
  order?: 'asc' | 'desc';
}
