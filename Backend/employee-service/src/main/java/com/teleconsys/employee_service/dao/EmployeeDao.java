package com.teleconsys.employee_service.dao;

import com.employees.crud.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

// Annotazione di Spring che indica che l'interfaccia è un componente di accesso ai dati e
// può essere utilizzata per interagire con il database
@Repository

// Data Access Object, pattern di progettazione che separa la logica di accesso ai dati
// dal resto dell'applicazione. Si tratta sostanzialmente di un'interfaccia che
// gestisce tutte le interazioni con il database.
// 'JPARepository' fornisce un set di metodi predefiniti per operazioni CRUD (Create, Read, Update, Delete)
public interface EmployeeDao extends JpaRepository<Employee, Integer>, QuerydslPredicateExecutor<Employee> {
    // Implementazione non necessaria perchè metodi utili già importati da CrudRepository
}
