export interface IWallet {
  id: number;
  transTypeClassId?: number | null;
  stock?: number | null;
  description?: string | null;
  deposit?: number | null;
  withdrawal?: number | null;
}

export type NewWallet = Omit<IWallet, 'id'> & { id: null };
