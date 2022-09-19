import { useContext } from "react";
import { DialogContext, DialogContextData } from "../providers/alert";

export function useDialog(): DialogContextData {
  const context = useContext(DialogContext);

  if (!context) {
    throw new Error("o useDialog deve ser utilizado dentro de um DialogProvider");
  }

  return context;
}