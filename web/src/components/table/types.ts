import { TableColumnHeaderProps } from "@chakra-ui/react";
import { ReactNode } from "react";

export interface Column<T> {
  title?: string;
  disableSkeleton?: boolean;
  options?: TableColumnHeaderProps
  property?: keyof T;
  defaultValue?: string;
  render?: (row: T) => ReactNode;
}

export interface Options<T> {
  loading?: boolean;
  variant?: 'simple' | 'striped' | 'unstyled';
  size?: 'sm' | 'md' | 'lg';
  content: T[];
  columns: Column<T>[];
}