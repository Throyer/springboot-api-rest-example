import { useState } from "react"
import { PaginationProps } from "../../components/pagination";
import { User } from "./use-user.types"

export const useUsers = () => {

  const [users, setUsers] = useState<User[]>([])
  const [loading, setLoading] = useState(false);
  const [pagination, setPagination] = useState<PaginationProps>({
    page: 0,
    size: 10,
    totalPages: 10
  });

  const update = (page?: number, size?: number) => {

  }

  return {
    users,
    pagination,
    loading,
    update
  }
}