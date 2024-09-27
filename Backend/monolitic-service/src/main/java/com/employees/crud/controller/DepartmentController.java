package com.employees.crud.controller;

import com.employees.crud.dao.DepartmentDao;
import com.employees.crud.entity.Department;
import com.employees.crud.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Annotazione per autorizzazione
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private DepartmentDao departmentDao;

    // POST per creare un nuovo dipartimento
    @PostMapping
    // @PreAuthorize("hasRole('HR')") // Limita l'accesso ai soli utenti HRs
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
    // @PreAuthorize("hasRole('HR')")
    public Department updateDepartment(@RequestBody Department department) {
        return departmentService.updateDepartment(department);
    }

    // DELETE per eliminare un dipartimento tramite ID - solo HR può farlo
    @DeleteMapping("/{departmentId}")
    // @PreAuthorize("hasRole('HR')")
    public void deleteDepartment(@PathVariable Integer departmentId) {
        departmentService.deleteDepartment(departmentId);
    }

    // GET per ottenere i dipartimenti con i rispettivi dipendenti - solo HR può farlo
    @GetMapping("/employees")
    // @PreAuthorize("hasRole('HR')")
    public ResponseEntity<Map<String, List<Map<String, Object>>>> getDepartmentsWithEmployees() {
        List<Object[]> results = departmentService.getDepartmentsWithEmployees();

        Map<String, List<Map<String, Object>>> departmentsMap = new HashMap<>();
        String currentDepartmentName = null;
        List<Map<String, Object>> employeesList = null;

        for (Object[] row : results) {
            String departmentName = (String) row[1];
            Integer employeeId = (Integer) row[2];

            if (!departmentName.equals(currentDepartmentName)) {
                if (employeesList != null) {
                    departmentsMap.put(currentDepartmentName, employeesList);
                }
                currentDepartmentName = departmentName;
                employeesList = new ArrayList<>();
            }

            if (employeeId != null) {
                Map<String, Object> employeeData = new HashMap<>();
                employeeData.put("employeeId", row[2]);
                employeeData.put("employeeName", row[3]);
                employeeData.put("employeeContactNumber", row[4]);
                employeeData.put("employeeAddress", row[5]);
                employeeData.put("employeeGender", row[6]);
                employeeData.put("employeeSkills", row[7]);
                employeeData.put("employeeEmail", row[8]);
                employeesList.add(employeeData);
            }
        }

        if (employeesList != null) {
            departmentsMap.put(currentDepartmentName, employeesList);
        }

        return new ResponseEntity<>(departmentsMap, HttpStatus.OK);
    }

    // Metodo per capitalizzare la prima lettera
    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1).toLowerCase();
    }
}
