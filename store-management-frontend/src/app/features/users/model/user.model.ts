export interface UserData {
  loginUser: string;
  name: string;
  password?: string; // Opcional si se usa el mismo DTO para editar sin cambiar pass
  document: number;
  roleId: number;
  storeId: number;
}

export interface UserResponse {
  id: number;
  loginUser: string;
  name: string;
  document: number;
  roleId: number;
  storeId: number;
}

// Interfaz para mapear la respuesta paginada de Spring Data JPA
export interface UserPageResponse {
  content: UserResponse[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}