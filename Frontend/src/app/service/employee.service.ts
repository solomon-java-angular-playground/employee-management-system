import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Employee } from '../model/employee.model';
import { Observable } from 'rxjs';
import { Department } from '../model/department.model';
import { switchMap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class EmployeeService {
  employeesApi = 'http://localhost:9085/employees';

  constructor(private http: HttpClient) {}

  public saveEmployee(
    employee: Employee,
    newDepartmentName?: string
  ): Observable<Employee> {
    // Se c'è un nuovo nome del dipartimento, viene salvato prima quello
    if (newDepartmentName) {
      return this.http
        .post<Department>('http://localhost:9080/departments', {
          name: newDepartmentName,
        })
        .pipe(
          switchMap((newDepartment) => {
            employee.departmentId = newDepartment.departmentId; // Associa il nuovo ID del dipartimento all'impiegato
            return this.http.post<Employee>(`${this.employeesApi}`, employee);
          })
        );
    } else {
      // Altrimenti, salva l'impiegato normalmente
      console.log('Entered saveEmployeeService with:', employee);
      return this.http.post<Employee>(`${this.employeesApi}`, employee);
    }
  }

  public updateEmployee(employee: Employee): Observable<Employee> {
    console.log('Entered updateEmployeeService with:', employee);
    return this.http.put<Employee>(`${this.employeesApi}/${employee.employeeId}`, employee);
  }

  public getEmployees(): Observable<Employee[]> {
    return this.http.get<Employee[]>(`${this.employeesApi}`);
  }

  public deleteEmployee(employeeId: number): Observable<any> {
    return this.http.delete(`${this.employeesApi}/${employeeId}`);
  }

  public getEmployee(employeeId: number) {
    return this.http.get<Employee>(`${this.employeesApi}/${employeeId}`);
  }
}
