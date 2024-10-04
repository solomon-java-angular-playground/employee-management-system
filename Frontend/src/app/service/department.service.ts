import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Department } from '../model/department.model';

@Injectable({
  providedIn: 'root',
})
export class DepartmentService {
  private departmentEmployeesApi =
    'http://localhost:9080/departments/employees';

  private departmentsApi = 'http://localhost:9080/departments';

  constructor(private http: HttpClient) {}

  saveDepartment(department: Department) {
    return this.http.post<Department>(this.departmentsApi, department);
  }

  getDepartmentEmployees(): Observable<any> {
    return this.http.get<any>(this.departmentEmployeesApi);
  }

  getDepartments(): Observable<Department[]> {
    return this.http.get<Department[]>(this.departmentsApi);
  }
}
