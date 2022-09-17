import { Box, Center, Skeleton } from "@chakra-ui/react";
import { Table as ChakraUiTable, TableContainer, Tbody, Td, Th, Thead, Tr } from "@chakra-ui/table";
import { Random } from "../../utils/random";
import { Pagination, PaginationProps } from "../pagination";
import { Body } from "./body";
import { Head } from "./head";
import { Options } from "./types";

interface TableProps<T> extends Options<T> {
  pagination?: PaginationProps
}

export function Table<T>({ loading, content, columns, size, variant, pagination }: TableProps<T>) {
  return (
    <Box>
      <TableContainer>
        <ChakraUiTable size={size} variant={variant}>
          <Head columns={columns} />
          <Body
            loading={loading}
            columns={columns}
            content={content}
          />
        </ChakraUiTable>
      </TableContainer>
      {pagination && (
        <Center my="12">
          <Pagination
            loading={pagination.loading}
            onChange={pagination.onChange}
            page={pagination.page}
            size={pagination.size}
            sizes={pagination.sizes}
            totalElements={pagination.totalElements}
            totalPages={pagination.totalPages}
          />
        </Center>
      )}
    </Box>
  );
}