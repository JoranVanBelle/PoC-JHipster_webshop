export interface IPricing {
  id?: number;
  pricingID?: number;
  name?: string;
  price?: number;
}

export const defaultValue: Readonly<IPricing> = {};
