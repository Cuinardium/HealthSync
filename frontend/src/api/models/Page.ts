

export interface Page<T> {
  current: number;
  last: number;
  size: number;
  content: T[];
}
