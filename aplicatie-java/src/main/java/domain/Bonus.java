package domain;

import jakarta.persistence.*;
import repository.HasId;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "bonuses")
public class Bonus implements HasId<UUID> {
    @Id
    @GeneratedValue
    private UUID id;

    private Double suma;
    private String motiv;
    private LocalDate data;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public Bonus() {}

    @Override
    public UUID getId() { return id; }
    @Override
    public void setId(UUID id) { this.id = id; }

    public Double getSuma() { return suma; }
    public void setSuma(Double suma) { this.suma = suma; }
    public String getMotiv() { return motiv; }
    public void setMotiv(String motiv) { this.motiv = motiv; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
}