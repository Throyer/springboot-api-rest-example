import {
  createContext,
  useContext,
  useState,
  PropsWithChildren,
  ReactNode,
  useRef
} from "react";

import {
  AlertDialog,
  AlertDialogBody,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogContent,
  AlertDialogOverlay,
  useDisclosure,
  Button,
  ButtonProps,
} from '@chakra-ui/react'

export interface DialogProps {
  title?: string | ReactNode;
  accept?: {
    title?: string;
    onClick?: () => void;
    options?: ButtonProps
  },
  decline?: {
    title?: string;
    onClick?: () => void;
    options?: ButtonProps
  },
  content?: ReactNode;
}

export interface DialogContextData {
  show: (content: DialogProps) => void;
}

export const DialogContext = createContext<DialogContextData>({} as DialogContextData);

export const DialogProvider = ({ children }: PropsWithChildren) => {
  const { isOpen, onOpen, onClose } = useDisclosure()

  const cancelRef = useRef()
  const [dialog, setDialog] = useState<DialogProps | null>(null);

  const show = (content: DialogProps) => {
    setDialog(content);
    onOpen();
  }

  return (
    <DialogContext.Provider value={{ show }}>
      {children}
      {dialog && (
        <AlertDialog
          isOpen={isOpen}
          leastDestructiveRef={cancelRef as any}
          onClose={onClose}
        >
          <AlertDialogOverlay>
            <AlertDialogContent>
              {typeof dialog.title === 'string' ? (
                <AlertDialogHeader fontSize='lg' fontWeight='bold'>
                  {dialog.title}
                </AlertDialogHeader>
              ) : dialog.title}

              <AlertDialogBody>
                {dialog.content}
              </AlertDialogBody>

              <AlertDialogFooter>
                <Button
                  ref={cancelRef as any}
                  onClick={() => {
                    onClose();
                    dialog?.decline?.onClick && dialog?.decline?.onClick()
                  }}
                  {...dialog?.decline?.options}>
                  {dialog.decline?.title || 'Yes'}
                </Button>
                <Button
                  colorScheme='red'
                  ml={3}
                  onClick={() => {
                    onClose()
                    dialog?.accept?.onClick && dialog?.accept?.onClick()
                  }}
                  {...dialog?.accept?.options}>
                  {dialog.accept?.title || 'No'}
                </Button>
              </AlertDialogFooter>
            </AlertDialogContent>
          </AlertDialogOverlay>
        </AlertDialog>
      )}
    </DialogContext.Provider>
  );
}