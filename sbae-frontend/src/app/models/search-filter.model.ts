export interface SearchFilter {
  title: string;
  description: string;
  severity: 'LOW' | 'MEDIUM' | 'HIGH';
  lastName: string;
  firstName: string;
  email: string;
}
