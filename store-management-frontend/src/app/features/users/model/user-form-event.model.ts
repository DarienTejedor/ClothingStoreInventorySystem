import { UserData } from './user-request.model';
import { UserInfoRequest } from './user-info-request.model';
import { UserPasswordRequest } from './user-password-request.model';
import { UserPermissionsRequest } from './user-permissions-request.model';

export type UserFormEvent =
  | { type: 'CREATE'; data: UserData }
  | { type: 'INFO'; data: UserInfoRequest }
  | { type: 'PASSWORD'; data: UserPasswordRequest }
  | { type: 'PERMISSIONS'; data: UserPermissionsRequest }
  | { type: 'CREATE'; data: UserData };