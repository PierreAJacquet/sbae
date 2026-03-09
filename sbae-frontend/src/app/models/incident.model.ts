import {Person} from './person.model';

export interface Incident {
  id: number;
  title: string;
  description: string;
  severity: string;
  person: Person;
  createdAt: Date;
}
