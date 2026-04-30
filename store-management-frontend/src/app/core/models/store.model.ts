export interface Address{
    city: string;
    locality: string;
    street: string;
}

export interface Store {
    id?: number;
    name: string;
    address: Address;
    phoneNumber: string;
    email: string;
}