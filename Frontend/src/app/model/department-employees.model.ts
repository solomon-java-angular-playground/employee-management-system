import { Employee } from './employee.model';

// Oggetto che usa i nomi dei dipartimenti come chiavi 
// e associa ogni chiave a un array di Employee
export interface DepartmentEmployees {
  [departmentName: string]: Employee[];
}
