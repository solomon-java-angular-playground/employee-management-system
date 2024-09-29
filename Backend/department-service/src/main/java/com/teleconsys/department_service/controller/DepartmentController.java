package com.teleconsys.department_service.controller;

import com.teleconsys.department_service.entity.Department;
import com.teleconsys.department_service.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    // POST per creare un nuovo dipartimento
    @PostMapping
    public Department saveDepartment(@RequestBody Department department) {
        // Capitalizza la prima lettera del nome del dipartimento
        String departmentName = department.getDepartmentName();
        if (departmentName != null && !departmentName.isEmpty()) {
            department.setDepartmentName(capitalizeFirstLetter(departmentName));
        }
        return departmentService.saveDepartment(department);
    }

    // GET per ottenere tutti i dipartimenti - accesso consentito a tutti
    @GetMapping
    public List<Department> getDepartments() {
        return departmentService.getDepartments();
    }

    // GET per ottenere un singolo dipartimento tramite ID - accesso consentito a tutti
    @GetMapping("/{departmentId}")
    public Department getDepartment(@PathVariable Integer departmentId) {
        return departmentService.getDepartment(departmentId);
    }

    // PUT per aggiornare un dipartimento esistente - solo HR può farlo
    @PutMapping
    public Department updateDepartment(@RequestBody Department department) {
        return departmentService.updateDepartment(department);
    }

    // DELETE per eliminare un dipartimento tramite ID - solo HR può farlo
    @DeleteMapping("/{departmentId}")
    public void deleteDepartment(@PathVariable Integer departmentId) {
        departmentService.deleteDepartment(departmentId);
    }

    // GET per ottenere i dipartimenti con i rispettivi dipendenti - solo HR può farlo
    @GetMapping("/employees")
    public ResponseEntity<List<Object[]>> getDepartmentsWithEmployees() {
        List<Object[]> departmentsWithEmployees = departmentService.getDepartmentsWithEmployees();
        return new ResponseEntity<>(departmentsWithEmployees, HttpStatus.OK);
    }

    // Metodo per capitalizzare la prima lettera
    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1).toLowerCase();
    }
}
