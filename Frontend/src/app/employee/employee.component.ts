import { Component, OnInit } from '@angular/core';
import { NgForOf, NgIf } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatRadioModule } from '@angular/material/radio';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDividerModule } from '@angular/material/divider';
import { MatButtonModule } from '@angular/material/button';
import { Employee } from '../model/employee.model';
import { Department } from '../model/department.model';
import { FormsModule, NgForm, NgModel } from '@angular/forms';
import { EmployeeService } from '../service/employee.service';
import { DepartmentService } from '../service/department.service';
import { HttpErrorResponse } from '@angular/common/http'; // Per gestire eventuali errori nelle richieste HTTP
import { ActivatedRoute, Router, RouterModule } from '@angular/router'; // Per la navigazione e il recupero di dati dalle rotte
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-employee',
  standalone: true,
  imports: [
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatSelectModule,
    MatRadioModule,
    MatCheckboxModule,
    MatDividerModule,
    MatButtonModule,
    FormsModule,
    RouterModule,
    NgForOf,
    NgIf,
  ],
  templateUrl: './employee.component.html',
  styleUrl: './employee.component.css',
})
export class EmployeeComponent implements OnInit {
  isCreateEmployee: boolean = true;
  employee: Employee = {} as Employee;
  skills: string[] = [];
  departments: Department[] = [];
  newDepartmentName: string = ''; // Nome del nuovo dipartimento, se inserito
  showNewDepartmentInput: boolean = false;
  createNewDepartment: string = 'no';

  // Per iniettare il servizio 'EmployeeService e i servizi di routing
  // 'Router' e 'ActivatedRoute' per la gestione delle rotte e il
  // recupero di parametri dall'URL
  constructor(
    private employeeService: EmployeeService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private departmentService: DepartmentService,
    private cdr: ChangeDetectorRef
  ) {}

  // Metodo eseguito quando il componente viene inizializzato
  ngOnInit(): void {
    // Recupera l'ID dell'impiegato dalla rotta
    this.activatedRoute.params.subscribe((params) => {
      const employeeId = params['employeeId'];

      if (employeeId) {
        // Carica i dati dell'impiegato se l'ID è presente
        this.loadEmployee(employeeId);
      } else {
        this.isCreateEmployee = true; // Modalità creazione
      }
    });

    // Recupero dei dipartimenti
    this.getDepartments();
  }

  private loadEmployee(employeeId: number): void {
    this.employeeService.getEmployee(employeeId).subscribe({
      next: (data: Employee) => {
        this.employee = data;
        this.isCreateEmployee = false;

        // Se esistono competenze associate all'impiegato, queste sono separate da una stringa
        if (this.employee.employeeSkills != '') {
          this.skills = this.employee.employeeSkills
            .split(',')
            .map((skill) => skill.trim());
        }

        // Imposta il dipartimento selezionato nel modulo
        if (this.employee.department && this.employee.department.departmentId) {
          this.employee.employeeDepartmentId =
            this.employee.department.departmentId;
        }
      },
      error: (err: HttpErrorResponse) => {
        console.log('Error loading employee:', err);
      },
    });
  }

  checkSkills(skill: string) {
    return (
      this.employee.employeeSkills != null &&
      this.employee.employeeSkills.includes(skill)
    );
  }

  checkGender(gender: string) {
    return (
      this.employee.employeeGender != null &&
      this.employee.employeeGender == gender
    );
  }

  saveEmployee(employeeForm: NgForm): void {
    if (this.createNewDepartment === 'yes' && this.newDepartmentName) {
      this.createDepartment(this.newDepartmentName).subscribe({
        next: (newDepartment: Department) => {
          this.employee.employeeDepartmentId =
            newDepartment.departmentId as number;
          this.saveOrUpdateEmployee(employeeForm);
        },
        error: (err: HttpErrorResponse) => {
          console.log('Error creating department:', err);
        },
      });
    } else {
      this.saveOrUpdateEmployee(employeeForm);
    }
  }

  private saveOrUpdateEmployee(employeeForm: NgForm): void {
    console.log('Entered saveOrUpdateEmployee method with:', this.employee);

    if (this.isCreateEmployee) {
      console.log('Creating new Employee:', this.employee);
      this.employeeService.saveEmployee(this.employee).subscribe({
        next: (res: Employee) => {
          employeeForm.reset();
          this.router.navigate(['/']);
        },
        error: (err: HttpErrorResponse) => {
          console.log(err);
        },
      });
    } else {
      console.log('Updating existing Employee:', this.employee);
      this.employeeService.updateEmployee(this.employee).subscribe({
        next: (res: Employee) => {
          this.router.navigate(['/']);
        },
        error: (err: HttpErrorResponse) => {
          console.log(err);
        },
      });
    }
  }

  private createDepartment(departmentName: string) {
    const newDepartment = { departmentName };
    return this.departmentService.saveDepartment(newDepartment);
  }

  selectGender(gender: string): void {
    this.employee.employeeGender = gender;
  }

  onSkillsChanges(event: any): void {
    if (event.checked) {
      this.skills.push(event.source.value);
    } else {
      this.skills.forEach((item, index) => {
        if (item == event.source.value) {
          // Con il metodo 'splice' si elimina l'elemento dall'array all'indice specificato
          // Il secondo parametro indica la rimozione di un solo elemento.
          this.skills.splice(index, 1);
        }
      });
    }
    this.employee.employeeSkills = this.skills.join(', ');
  }

  onCreateNewDepartmentChange(value: string): void {
    this.createNewDepartment = value;
    this.cdr.detectChanges(); // Forza il rilevamento delle modifiche
  }

  getDepartments(): void {
    this.departmentService.getDepartments().subscribe({
      next: (res: Department[]) => {
        this.departments = res;
      },
      error: (err: HttpErrorResponse) => {
        console.log('Error fetching departments:', err);
      },
    });
  }
}
