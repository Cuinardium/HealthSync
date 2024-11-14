export interface Specialty {
  name: string;
  code: string;
  popularity: number;
}

export interface SpecialtyQuery {
  sort?: 'popularity' | 'standard';
  order?: 'asc' | 'desc';
}
