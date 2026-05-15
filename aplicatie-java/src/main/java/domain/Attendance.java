package domain;

import jakarta.persistence.*;
import repository.HasId;

import java.util.UUID;

@Entity
@Table(name = "attendance")
public class Attendance implements HasId<UUID> {
    @Id
    @GeneratedValue
    private UUID id;

    private String luna;
    private Integer zileLucratoareLuna;
    private Integer zileAbsente;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public Attendance() {}

    @Override
    public UUID getId() { return id; }
    @Override
    public void setId(UUID id) { this.id = id; }

    public String getLuna() { return luna; }
    public void setLuna(String luna) { this.luna = luna; }
    public Integer getZileLucratoareLuna() { return zileLucratoareLuna; }
    public void setZileLucratoareLuna(Integer zileLucratoareLuna) { this.zileLucratoareLuna = zileLucratoareLuna; }
    public Integer getZileAbsente() { return zileAbsente; }
    public void setZileAbsente(Integer zileAbsente) { this.zileAbsente = zileAbsente; }
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
}