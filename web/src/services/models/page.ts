export interface Page<T> {
  content: T[];
  page: number;
  size: number;
  totalPages: number;
  totalElements: number;
}