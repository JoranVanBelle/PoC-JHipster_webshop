export interface IInventory {
  id?: number;
  inventoryID?: number;
  name?: string;
  quantity?: number;
}

export const defaultValue: Readonly<IInventory> = {};
