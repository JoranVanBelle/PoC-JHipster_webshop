export interface ITransport {
  id?: number;
  transportID?: number;
  transportName?: string;
}

export const defaultValue: Readonly<ITransport> = {};
