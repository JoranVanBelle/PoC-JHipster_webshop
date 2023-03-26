export interface IOrder {
  id?: number;
  orderID?: number;
  orderPrice?: number;
  orderQuantity?: number;
  username?: string;
  transportID?: number;
}

export const defaultValue: Readonly<IOrder> = {};
