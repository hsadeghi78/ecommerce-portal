import { IUserFavorite, NewUserFavorite } from './user-favorite.model';

export const sampleWithRequiredData: IUserFavorite = {
  id: 5146,
};

export const sampleWithPartialData: IUserFavorite = {
  id: 19613,
};

export const sampleWithFullData: IUserFavorite = {
  id: 28682,
};

export const sampleWithNewData: NewUserFavorite = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
