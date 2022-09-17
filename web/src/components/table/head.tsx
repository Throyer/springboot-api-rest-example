import { Th, Thead, Tr } from "@chakra-ui/table";
import { Column } from "./types";

interface HeadProps<T> {
  columns: Column<T>[];
}

export function Head<T>({ columns }: HeadProps<T>) {
  return (
    <Thead>
      <Tr>
        {columns.map(({ title: name, property, options }, index) => (
          <Th {...options} key={index}>
            {name ?? property?.toString()}
          </Th>
        ))}
      </Tr>
    </Thead>
  );
}