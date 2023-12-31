import { IFileDocument, NewFileDocument } from './file-document.model';

export const sampleWithRequiredData: IFileDocument = {
  id: 1674,
  fileName: 'as',
  fileContent: '../fake-data/blob/hipster.png',
  fileContentContentType: 'unknown',
  description: 'even circa',
};

export const sampleWithPartialData: IFileDocument = {
  id: 15436,
  fileName: 'um',
  fileContent: '../fake-data/blob/hipster.png',
  fileContentContentType: 'unknown',
  description: 'since roughly',
};

export const sampleWithFullData: IFileDocument = {
  id: 156,
  fileName: 'modulo revisit capon',
  fileContent: '../fake-data/blob/hipster.png',
  fileContentContentType: 'unknown',
  filePath: 'endow yowza deceivingly',
  description: 'which',
};

export const sampleWithNewData: NewFileDocument = {
  fileName: 'progress husky',
  fileContent: '../fake-data/blob/hipster.png',
  fileContentContentType: 'unknown',
  description: 'rehear after lovingly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
