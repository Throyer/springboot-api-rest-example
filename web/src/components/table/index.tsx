import { ReactNode } from "react";

import { Table as ChakraUiTable, TableContainer, Tbody, Td, Th, Thead, Tr, TableHeadProps, TableColumnHeaderProps } from "@chakra-ui/table";

export interface Column<T> {
  title?: string;
  options?: TableColumnHeaderProps
  property?: keyof T;
  defaultValue?: string;
  render?: (row: T) => ReactNode;
}

export interface Options<T> {
  content: T[];
  columns: Column<T>[];
}

export function Table<T>({ content, columns }: Options<T>) {
  return (
    <TableContainer>
      <ChakraUiTable>
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
          {content.map((row, index) => (
            <Tr key={index}>
              {columns.map(
                ({ render, property, defaultValue }, prop) => {
                  if (property) {
                    const types = ["number", "boolean", "string"];
                    const value = row[property];

                    if (value && types.includes(typeof value)) {
                      return <Td key={prop}>{`${value}`}</Td>;
                    }
                  }

                  if (render) {
                    const element = render(row);
                    if (element) {
                      return <Td key={prop}>{element}</Td>;
                    }
                  }

                  return <Td key={prop}>{defaultValue ?? ""}</Td>;
                }
              )}
            </Tr>
          ))}
        </Tbody>
      </ChakraUiTable>
    </TableContainer>
  );
}