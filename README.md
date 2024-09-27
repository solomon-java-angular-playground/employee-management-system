# Employee Management System

## Description

This project is a small management system that allows you to view, update, and delete information about company employees. The employee data includes:

- ID
- First and Last Name
- Phone Number
- Residence
- Department
- Skills (Angular, Java, Word, Excel)

The project is divided into two parts:

1. **Backend Java Spring Boot**: Provides REST APIs to manage employees.
2. **Frontend Angular**: Allows the visualization and management of employee data through a user interface built with Angular Material.

## Technologies Used

- **Backend**:
  - Java Spring Boot
  - MySQL as the database
  - Spring Data JPA for database interaction

- **Frontend**:
  - Angular 8
  - Angular Material for design and components
  - HttpClient for communication with the backend

## Features

- **Add new employee**: You can insert a new employee to the database by filling the form.
- **View employee list**: The data is displayed in a table using Material Design components.
- **Update employee data**: You can update employee information directly from the interface.
- **Delete employee**: Users can remove an employee from the list.

## Project Structure

```bash
/ (root)
├── Backend Java Spring Boot/   # Contains the backend code
└── Frontend Angular/           # Contains the frontend code
```

## Installation

### Prerequisites

- Node.js and Angular CLI installed for the frontend
- JDK 11+ and Maven for the backend
- MySQL for the database

### Backend Configuration

1. Clone the **Backend Java Spring Boot** folder.
2. Create a MySQL database named `employee_management`.
3. Update the database credentials in `application.properties`.
4. Start the backend with the command:

   ```bash
   mvn spring-boot:run
   ```

### Frontend Configuration

1. Clone the **Frontend Angular** folder.
2. Install the dependencies with the command:

   ```bash
   npm install
   ```

3. Start the frontend with:

   ```bash
   ng serve
   ```

## Usage

After starting the project, you can access the frontend application by visiting `http://localhost:4200` and interacting with the employee list.

<hr>

### Italiano:

# Sistema di Gestione Dipendenti

## Descrizione

Questo progetto è un piccolo gestionale che permette di visualizzare, aggiornare e cancellare informazioni sui dipendenti di un'azienda. I dati dei dipendenti includono:

- ID
- Nome e Cognome
- Numero di telefono
- Residenza
- Dipartimento
- Skill (Angular, Java, Word, Excel)

Il progetto è diviso in due parti:

1. **Backend Java Spring Boot**: Fornisce le API REST per gestire i dipendenti.
2. **Frontend Angular**: Consente la visualizzazione e la gestione dei dati tramite un'interfaccia utente realizzata con Angular Material.

## Tecnologie Utilizzate

- **Backend**:
  - Java Spring Boot
  - MySQL come database
  - Spring Data JPA per l'interazione con il database

- **Frontend**:
  - Angular 8
  - Angular Material per il design e i componenti
  - HttpClient per la comunicazione con il backend

## Funzionalità

- **Aggiunta di nuovo dipendente**: È possibile inserire nel database un nuovo dipendente con il form apposito.
- **Visualizzazione della lista dei dipendenti**: I dati vengono mostrati in una tabella con i componenti di Material Design.
- **Modifica dei dati**: È possibile aggiornare le informazioni di un dipendente direttamente dall'interfaccia.
- **Cancellazione di un dipendente**: Gli utenti possono rimuovere un dipendente dal database e, dunque, dalla lista di visualizzazione.

## Struttura del Progetto

```bash
/ (root)
├── Backend Java Spring Boot/   # Contiene il codice del backend
└── Frontend Angular/           # Contiene il codice del frontend
```

## Installazione

### Prerequisiti

- Node.js e Angular CLI installati per il frontend
- JDK 11+ e Maven per il backend
- MySQL per il database

### Configurazione Backend

1. Clona la cartella **Backend Java Spring Boot**.
2. Crea un database MySQL chiamato `employee_management`.
3. Aggiorna le credenziali del database in `application.properties`.
4. Avvia il backend con il comando:

   ```bash
   mvn spring-boot:run
   ```

### Configurazione Frontend

1. Clona la cartella **Frontend Angular**.
2. Installa le dipendenze con il comando:

   ```bash
   npm install
   ```

3. Avvia il frontend con:

   ```bash
   ng serve
   ```

## Utilizzo

Dopo aver avviato il progetto, puoi accedere all'applicazione frontend visitando `http://localhost:4200` e interagire con la lista dei dipendenti.
