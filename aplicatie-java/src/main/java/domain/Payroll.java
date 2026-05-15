package domain;

import jakarta.persistence.*;
import repository.HasId;

import java.util.UUID;

@Entity
@Table(name = "payroll")
public class Payroll implements HasId<UUID> {
    @Id
    @GeneratedValue
    private UUID id;

    private String luna;
    private Double salariuFinal;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public Payroll() {}

    @Override
    public UUID getId() { return id; }
    @Override
    public void setId(UUID id) { this.id = id; }

    public String getLuna() { return luna; }
    public void setLuna(String luna) { this.luna = luna; }
    public Double getSalariuFinal() { return salariuFinal; }
    public void setSalariuFinal(Double salariuFinal) { this.salariuFinal = salariuFinal; }
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
}