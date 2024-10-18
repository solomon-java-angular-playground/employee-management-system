package com.teleconsys.department_service.controller;

import com.teleconsys.department_service.dto.DepartmentDTO;
import com.teleconsys.department_service.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:4200") // Per accettare richieste provenienti da una specifica origine
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    // POST per creare un nuovo dipartimento
    @PostMapping
    public ResponseEntity<DepartmentDTO> saveDepartment(@RequestBody DepartmentDTO departmentDto) {
        // Capitalizza la prima lettera del nome del dipartimento
        String departmentName = departmentDto.getDepartmentName();
        if (departmentName != null && !departmentName.isEmpty()) {
            departmentDto.setDepartmentName(capitalizeFirstLetter(departmentName));
        }

        // Salva il dipartimento e restituisci il DTO del dipartimento salvato
        DepartmentDTO savedDepartment = departmentService.saveDepartment(departmentDto);
        return new ResponseEntity<>(savedDepartment, HttpStatus.CREATED);
    }

    // GET per ottenere tutti i dipartimenti - accesso consentito a tutti
    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> getDepartments() {
        List<DepartmentDTO> departments = departmentService.getDepartments();
        return new ResponseEntity<>(departments, HttpStatus.OK);
    }

    // GET per ottenere un singolo dipartimento tramite ID - accesso consentito a tutti
    @GetMapping("/{departmentId}")
    public ResponseEntity<DepartmentDTO> getDepartment(@PathVariable Integer departmentId) {
        DepartmentDTO department = departmentService.getDepartment(departmentId);
        return new ResponseEntity<>(department, HttpStatus.OK);
    }

    // PUT per aggiornare un dipartimento esistente - solo HR può farlo
    @PutMapping
    public ResponseEntity<DepartmentDTO> updateDepartment(@RequestBody DepartmentDTO departmentDto) {
        DepartmentDTO updatedDepartment = departmentService.updateDepartment(departmentDto);
        return new ResponseEntity<>(updatedDepartment, HttpStatus.OK);
    }

    // DELETE per eliminare un dipartimento tramite ID - solo HR può farlo
    @DeleteMapping("/{departmentId}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Integer departmentId) {
        departmentService.deleteDepartment(departmentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // GET per ottenere i dipartimenti con i rispettivi dipendenti - solo HR può farlo
    @GetMapping("/employees")
    public ResponseEntity<List<Map<String, Object>>> getDepartmentsWithEmployees() {
        List<Map<String, Object>> departmentsWithEmployees = departmentService.getDepartmentsWithEmployees();
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
