import { Box, Skeleton } from "@chakra-ui/react";
import { Tbody, Td, Tr } from "@chakra-ui/table";
import { Arrays } from "../../utils/arrays";
import { Random } from "../../utils/random";
import { Column } from "./types";

interface BodyProps<T> {
  loading?: boolean;
  columns: Column<T>[];
  content: T[];
}

export function Body<T>({ loading, columns, content }: BodyProps<T>): JSX.Element {

  if (Arrays.isNullOrEmpty(content)) {
    return (
      <Tbody>
        {Array(10)
          .fill(0)
          .map((_, row) => (
            <Tr key={row}>
              {columns.map((_, index) => (
                <Td key={index}>
                  <Box py="2.5">
                    <Skeleton height="13px" borderRadius="3" width={`${Random.between(25, 100)}%`} />
                  </Box>
                </Td>
              ))}
            </Tr>
          ))}
      </Tbody>
    )
  }

  const render = function <T>(row: T, column: Column<T>, index: number) {
    const { render, property, defaultValue, disableSkeleton } = column;
    if (loading && !disableSkeleton) {
      return (
        <Td key={index}>
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
        return <Td key={index}>{`${value}`}</Td>;
      }
    }

    if (render) {
      const element = render(row);
      if (element) {
        return <Td key={index}>{element}</Td>;
      }
    }

    return <Td key={index}>{defaultValue ?? ""}</Td>;
  }

  return (
    <Tbody>
      {content.map((row, key) => (
        <Tr key={key}>
          {columns.map((column, index) => render(row, column, index))}
        </Tr>
      ))}
    </Tbody>
  );
}