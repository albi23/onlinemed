import {BaseEntity} from './core-model';

export interface DrugHints {
  name: string;
  redirectUrl: string;
}

export interface DrugInfo {
  box: string;
  dose: string;
  drugForm: string;
  drugName: string;
  fullPrice: string;
  patientPrice: string;
  payment: string;
  urlEquivalent: string;
}

export interface ErrorMessage {
  entity: BaseEntity;
  errorID: string;
  message: string;
  stackTrace: string;
  timestamp: string;
  url: string;
}

export interface Mail {
  body: string;
  name: string;
  receiverMail: string;
  senderMail: string;
  subject: string;
  surname: string;
}

export interface Violation {
  errorUIkey: string;
  param: undefined | ViolationParam[];
  paramList: ViolationParam[];
}

export interface ViolationParam {
  name: string;
  value: any;
}

