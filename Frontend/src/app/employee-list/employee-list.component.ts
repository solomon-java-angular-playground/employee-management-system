import { Component, OnInit } from '@angular/core';
import { EmployeeService } from '../service/employee.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Employee } from '../model/employee.model';
import { MatButtonModule } from '@angular/material/button';
import { Router, RouterModule } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-employee-list',
  standalone: true,
  imports: [MatButtonModule, RouterModule, MatTableModule, MatIconModule],
  templateUrl: './employee-list.component.html',
  styleUrl: './employee-list.component.css',
})
export class EmployeeListComponent implements OnInit {
  // Le colonne da visualizzare nella tabella
  displayedColumns: string[] = [
    'employeeId',
    'employeeName',
    'employeeEmail',
    'employeeContactNumber',
    'employeeAddress',
    'employeeDepartment',
    'employeeGender',
    'employeeSkills',
    'update',
    'delete',
  ];
  // Array che conterrà i dati dell'impiegato
  dataSource: Employee[] = [];

  // Costruttore che inietta i servizi necessari e chiama 'getEmployeeList()'
  // per popolare i dati
  constructor(
    private employeeService: EmployeeService,
    private router: Router
  ) {
    this.getEmployeeList();
  }

  ngOnInit(): void {}

  // Per navigare alla pagina di aggiornamento dell'impiegato quando viene cliccata
  // l'icona di modifica
  updateEmployee(employeeId: number): void {
    this.router.navigate(['/employee', employeeId]);
  }

  deleteEmployee(employeeId: number): void {
    this.employeeService.deleteEmployee(employeeId).subscribe({
      next: (res) => {
        console.log(res);
        this.getEmployeeList();
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
    });
  }

  getEmployeeList(): void {
    this.employeeService.getEmployees().subscribe({
      next: (res: Employee[]) => {
        this.dataSource = res;
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
    });
  }
}
