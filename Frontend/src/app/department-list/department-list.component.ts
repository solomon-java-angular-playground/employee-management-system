import { Component, OnInit } from '@angular/core';
import { NgForOf } from '@angular/common';
import { NgIf } from '@angular/common';
import { DepartmentService } from '../service/department.service';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatListModule } from '@angular/material/list';
import { DepartmentEmployees } from '../model/department-employees.model';
import { HttpErrorResponse } from '@angular/common/http';
import { MatIcon } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';

@Component({
  selector: 'app-department-list',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    MatCardModule,
    MatDividerModule,
    MatListModule,
    MatIcon,
    MatTableModule,
  ],
  templateUrl: './department-list.component.html',
  styleUrls: ['./department-list.component.css'],
})
export class DepartmentListComponent implements OnInit {
  departmentEmployees: DepartmentEmployees = {};
  displayedColumns: string[] = [
    'employeeName',
    'employeeEmail',
    'employeeContactNumber',
    'employeeAddress',
    'employeeSkills',
    'employeeGender',
  ];

  constructor(private departmentService: DepartmentService) {}

  ngOnInit(): void {
    this.getDepartmentEmployees();
  }

  getDepartmentEmployees(): void {
    this.departmentService.getDepartmentEmployees().subscribe({
      next: (res: DepartmentEmployees) => {
        console.log('Data received from getDepartment service: ', res);
        this.departmentEmployees = res;
      },
      error: (err: HttpErrorResponse) => {
        console.error('Error fetching department employees:', err);
      },
    });
  }

  // Funzione per ottenere i nomi dei dipartimenti
  getDepartmentNames(): string[] {
    return Object.keys(this.departmentEmployees);
  }
}
