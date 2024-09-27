import {
  ActivatedRouteSnapshot,
  ResolveFn,
  RouterStateSnapshot,
} from '@angular/router';
import { EmployeeService } from './service/employee.service';
import { inject } from '@angular/core';
import { Observable, of } from 'rxjs';
import { Employee } from './model/employee.model';

export const EmployeeResolver: ResolveFn<any> = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot,
  employeeService: EmployeeService = inject(EmployeeService)
): Observable<Employee> => {
  const employeeId = route.paramMap.get('employeeId');
  if (employeeId) {
    return employeeService.getEmployee(Number(employeeId));
  } else {
    // Creare un oggetto vuoto se non è presente alcun ID dell'impiegato
    const employee: Employee = {
      departmentId: null,
      department: null,
      employeeId: 0,
      employeeName: '',
      employeeContactNumber: '',
      employeeAddress: '',
      employeeGender: '',
      employeeDepartmentId: 0,
      employeeSkills: '',
      employeeEmail: '',
    };

    return of(employee);
  }
};
