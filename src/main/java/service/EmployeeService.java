package service;

import domain.*;
import repository.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class EmployeeService {
    private RepositoryDBEmployee employeeRepo;
    private RepositoryDBBonus bonusRepo;
    private RepositoryDBPenalty penaltyRepo;
    private RepositoryDBPayroll payrollRepo;

    public EmployeeService(RepositoryDBEmployee employeeRepo, RepositoryDBBonus bonusRepo,
                           RepositoryDBPenalty penaltyRepo, RepositoryDBPayroll payrollRepo) {
        this.employeeRepo = employeeRepo;
        this.bonusRepo = bonusRepo;
        this.penaltyRepo = penaltyRepo;
        this.payrollRepo = payrollRepo;
    }

    // F0. Import din CSV
    public boolean importEmployeesFromCSV(String path) throws IOException {
        boolean foundDuplicates = false;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    String nume = data[0].trim();
                    // Verificam daca exista deja
                    if (employeeRepo.getAll().stream().anyMatch(e -> e.getNume().equalsIgnoreCase(nume))) {
                        foundDuplicates = true;
                        continue; // Sarim peste el daca e duplicat la importul simplu
                    }
                    Employee emp = new Employee(nume, data[1].trim(), Double.parseDouble(data[2].trim()));
                    employeeRepo.save(emp);
                    calculateSalarySimplified(emp, "Mai-2026");
                }
            }
        }
        return foundDuplicates;
    }

    // Functie pentru adaugare manuala
    public void saveEmployee(String nume, String functie, Double salariuZi) {
        Employee emp = new Employee(nume, functie, salariuZi);
        employeeRepo.save(emp);
        calculateSalarySimplified(emp, "Mai-2026");
    }

    //functie de update

    // Verifica dacă un angajat exista deja după nume
    public boolean exists(String nume) {
        return employeeRepo.getAll().stream()
                .anyMatch(e -> e.getNume().equalsIgnoreCase(nume.trim()));
    }

    // F2. Vizualizare - Foloseste getAll() din repo
    public List<Employee> getAllEmployees() {
        return employeeRepo.getAll();
    }

    // F2. Filtrare - Foloseste findByCriteria()
    public List<Employee> filterEmployees(String criteria, String value) {
        return employeeRepo.findByCriteria(criteria, value);
    }

    // F3. Modificare
    public void updateEmployee(Employee employee) {
        employeeRepo.update(employee);
        calculateSalarySimplified(employee, "Mai-2026");
    }

    // F6. Adaugare Bonus
    public void addBonus(Employee emp, Double suma, String motiv) {
        Bonus bonus = new Bonus();
        bonus.setEmployee(emp);
        bonus.setSuma(suma);
        bonus.setMotiv(motiv);
        bonus.setData(LocalDate.now());
        bonusRepo.save(bonus);

        calculateSalarySimplified(emp, "Mai-2026");
    }

    // F7. Adaugare Penalizare
    public void addPenalty(Employee emp, Double suma, String motiv) {
        Penalty penalty = new Penalty();
        penalty.setEmployee(emp);
        penalty.setSuma(suma);
        penalty.setMotiv(motiv);
        penalty.setData(LocalDate.now());
        penaltyRepo.save(penalty);

        calculateSalarySimplified(emp, "Mai-2026");
    }

    /**
     * 20 zile lucrate implicit x 8 ore => salariu pe zi
     */
    private void calculateSalarySimplified(Employee emp, String luna) {
        double salariuBaza = emp.getSalariuPeZi() * 20;

        // Filtram bonusurile pentru acest angajat din lista totala (metoda getAll exista in repo)
        double totalBonus = bonusRepo.getAll().stream()
                .filter(b -> b.getEmployee().getId().equals(emp.getId()))
                .mapToDouble(Bonus::getSuma)
                .sum();

        // Filtram penalizarile
        double totalPenalty = penaltyRepo.getAll().stream()
                .filter(p -> p.getEmployee().getId().equals(emp.getId()))
                .mapToDouble(Penalty::getSuma)
                .sum();

        double sumaFinala = salariuBaza + totalBonus - totalPenalty;

        // Cautam daca exista deja un payroll pentru acest angajat in luna respectiva
        Payroll payroll = payrollRepo.getAll().stream()
                .filter(p -> p.getEmployee().getId().equals(emp.getId()) && p.getLuna().equals(luna))
                .findFirst()
                .orElse(null);

        if (payroll == null) {
            payroll = new Payroll();
            payroll.setEmployee(emp);
            payroll.setLuna(luna);
            payroll.setSalariuFinal(sumaFinala);
            payrollRepo.save(payroll);
        } else {
            payroll.setSalariuFinal(sumaFinala);
            payrollRepo.update(payroll);
        }
    }

    public Double getFinalSalaryForEmployee(Employee emp, String luna) {
        return payrollRepo.getAll().stream()
                .filter(p -> p.getEmployee().getId().equals(emp.getId()) && p.getLuna().equals(luna))
                .map(Payroll::getSalariuFinal)
                .findFirst()
                .orElse(emp.getSalariuPeZi() * 20); // Fallback la calculul de baza daca nu s-a generat payroll
    }
}