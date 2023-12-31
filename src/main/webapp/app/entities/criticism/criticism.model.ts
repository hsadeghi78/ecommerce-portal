export interface ICriticism {
  id: number;
  fullName?: string | null;
  email?: string | null;
  contactNumber?: string | null;
  description?: string | null;
}

export type NewCriticism = Omit<ICriticism, 'id'> & { id: null };
