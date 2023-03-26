import { IUser } from 'app/shared/model/user.model';

export interface ICustomer {
  id?: number;
  username?: string;
  address?: string;
  user?: IUser;
}

export const defaultValue: Readonly<ICustomer> = {};
