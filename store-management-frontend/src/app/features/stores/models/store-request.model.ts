import { Address } from "./store.model";

export interface StoreRequest {
  name: string;
  address: Address;
  phoneNumber: string;
  email: string;
}