import { IUserComment, NewUserComment } from './user-comment.model';

export const sampleWithRequiredData: IUserComment = {
  id: 23839,
  rating: 31597.83,
  visible: true,
};

export const sampleWithPartialData: IUserComment = {
  id: 13466,
  rating: 14056.32,
  visible: false,
};

export const sampleWithFullData: IUserComment = {
  id: 9646,
  rating: 24004.99,
  visible: true,
  description: 'kiddingly ick',
};

export const sampleWithNewData: NewUserComment = {
  rating: 1705.3,
  visible: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
