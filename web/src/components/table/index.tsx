import { ReactNode } from "react";

import { Table as ChakraUiTable, TableContainer, Tbody, Td, Th, Thead, Tr, TableHeadProps, TableColumnHeaderProps } from "@chakra-ui/table";
import { Box, Skeleton } from "@chakra-ui/react";
import { Random } from "../../utils/random";

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

export function Table<T>({ loading, content, columns, size, variant }: Options<T>) {
  return (
    <TableContainer>
      <ChakraUiTable size={size} variant={variant}>
        <Thead>
          <Tr>
            {columns.map(({ title: name, property, options }, index) => (
              <Th {...options} key={index}>
                {name ?? property?.toString()}
              </Th>
            ))}
          </Tr>
        </Thead>
        <Tbody>
          {content && content.length === 0 && (
            Array(10)
              .fill(0)
              .map((_, rowIndex) => (
                <Tr key={rowIndex}>
                  {columns.map((_, columnIndex) => (
                    <Td key={columnIndex}>
                      <Box py="2">
                        <Skeleton height="12px" borderRadius="3" width={`${Random.between(25, 100)}%`} />
                      </Box>
                    </Td>
                  ))}
                </Tr>
              ))
          )}
          {content.map((row, index) => (
            <Tr key={index}>
              {columns.map(
                ({ render, property, defaultValue, disableSkeleton }, tdIndex) => {
                  if (loading && !disableSkeleton) {
                    return (
                      <Td key={tdIndex}>
                        <Box py="2.5">
                          <Skeleton height="13px" borderRadius="3" width={`${Random.between(25, 100)}%`} />
                        </Box>
                      </Td>
                    )
                  }

                  if (property) {
                    const types = ["number", "boolean", "string"];
                    const value = row[property];

                    if (value && types.includes(typeof value)) {
                      return <Td key={tdIndex}>{`${value}`}</Td>;
                    }
                  }

                  if (render) {
                    const element = render(row);
                    if (element) {
                      return <Td key={tdIndex}>{element}</Td>;
                    }
                  }

                  return <Td key={tdIndex}>{defaultValue ?? ""}</Td>;
                }
              )}
            </Tr>
          ))}
        </Tbody>
      </ChakraUiTable>
    </TableContainer>
  );
}