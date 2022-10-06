import { Button, ButtonProps, HStack } from "@chakra-ui/react";
import { PropsWithChildren } from "react";
import { BsFillPenFill, BsTrash2, BsEye } from 'react-icons/bs';
import { Column } from "./types";

type CustomButtonProps<T> = {
  onClick?: (row?: T) => void;
  options?: Omit<ButtonProps, 'onClick'>;
}

interface ActionsProps<T> {
  row?: T,
  show?: CustomButtonProps<T>;
  edit?: CustomButtonProps<T>;
  remove?: CustomButtonProps<T>;
}

export const Actions = function <T>({ row, show, edit, remove, children }: ActionsProps<T> & PropsWithChildren) {
  return (
    <HStack>
      {show && (
        <Button
          size="sm"
          variant="outline"
          {...show.options}
          onClick={() => show.onClick && show.onClick(row)}>
          <BsEye />
        </Button>
      )}
      {edit && (
        <Button
          size="sm"
          variant="outline"          
          {...edit.options}
          onClick={() => edit.onClick && edit.onClick(row)}>
          <BsFillPenFill />
        </Button>
      )}
      {remove && (
        <Button
          size="sm"
          variant="outline"          
          {...remove.options}
          onClick={() => remove.onClick && remove.onClick(row)}>
          <BsTrash2 />
        </Button>
      )}
      {children}
    </HStack>
  )
}

export const actions = function <T>(props: ActionsProps<T>): Column<T> {
  return ({
    title: 'Actions',
    options: { w: '28' },
    disableSkeleton: true,
    render: (row) => <Actions row={row} {...props} />
  })
}