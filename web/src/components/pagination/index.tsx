import { Button } from "@chakra-ui/button";
import { ButtonGroup, Center, Select, Spinner, Stack } from "@chakra-ui/react";
import { ReactNode } from "react";

export interface PaginationProps {
  loading?: boolean;
  page?: number;
  size?: number;
  totalPages?: number;
  totalElements?: number;
  sizes?: number[];
  onChange?: (page: number, size: number) => void;
}

export const Pagination = ({
  onChange,
  loading,
  page = 1,
  size = 10,
  totalPages = 1,
  totalElements = 0,
  sizes = [10, 20, 25, 50]
}: PaginationProps) => {
  const intervalNumbers = (x: number, y: number): number[] => {
    let result = [];
    let i = x + 1;
    while (i < y) {
      result.push(i);
      i++;
    }
    return result;
  };

  const interval = (numbers: number[]): ReactNode => {
    const validNumbers = numbers.filter(item => item > -1 && item < totalPages);
    const buttons = validNumbers.map(intervalNumber => (
      <Button
        key={intervalNumber}
        onClick={() => handleChange(intervalNumber, size)}
        variant={page === intervalNumber ? 'solid' : 'outline'}
        disabled={intervalNumber === page}>
        {intervalNumber + 1}
      </Button>
    ));
    return buttons;
  }

  const handleChange = (page: number, size: number) => {
    onChange && onChange(page, size);
  }

  if (loading) {
    return (
      <Stack direction="row" spacing="5">
        <Center>
          <Spinner color="gray" />
        </Center>
        <Select
          disabled
          borderRadius="5"
          w="52"
          size="sm"
          placeholder='Itens por pagina'
        />
      </ Stack>
    )
  }

  return (
    <Stack direction="row" spacing="5">
      <ButtonGroup size='sm' isAttached variant="outline">
        <Button
          onClick={() => handleChange(0, size)}
          key="Primeira"
          disabled={page === 0}>
          Primeira
        </Button>

        {page > 0 && (
          <Button
            onClick={() => handleChange(page - 1, size)}
            key="Anterior"
            disabled={page === 0}>
            Anterior
          </Button>
        )}

        {interval(intervalNumbers(page - 3, page + 3))}

        {page + 1 < totalPages && (
          <Button
            onClick={() => handleChange(page + 1, size)}
            key="Proxima"
            disabled={page === totalPages}>
            Proxima
          </Button>
        )}

        <Button
          onClick={() => handleChange(totalPages - 1, size)}
          key="Ultima"
          disabled={page + 1 === totalPages}>
          Ultima
        </Button>
      </ButtonGroup>

      <Select
        onChange={({ target: { value } }) => handleChange(0, Number(value))}
        borderRadius="5"
        w="52"
        size="sm"
        placeholder='Itens por pagina'>
        {sizes.map(option => (
          <option key={option} value={option}>{option}</option>
        ))}
      </Select>
    </Stack>
  )
}