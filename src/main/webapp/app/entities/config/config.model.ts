export interface IConfig {
  id: number;
  displayName?: string | null;
  code?: string | null;
  value?: string | null;
}

export type NewConfig = Omit<IConfig, 'id'> & { id: null };
