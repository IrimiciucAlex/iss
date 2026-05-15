package domain;

import jakarta.persistence.*;
import repository.HasId;

import java.util.UUID;

@Entity
@Table(name = "employees")
public class Employee implements HasId<UUID> {
    @Id
    @GeneratedValue
    private UUID id;

    private String nume;
    private String functie;
    private Double salariuPeZi;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    public Employee() {}

    public Employee(String nume, String functie, Double salariuPeZi) {
        this.nume = nume;
        this.functie = functie;
        this.salariuPeZi = salariuPeZi;
    }

    @Override
    public UUID getId() { return id; }
    @Override
    public void setId(UUID id) { this.id = id; }

    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }
    public String getFunctie() { return functie; }
    public void setFunctie(String functie) { this.functie = functie; }
    public Double getSalariuPeZi() { return salariuPeZi; }
    public void setSalariuPeZi(Double salariuPeZi) { this.salariuPeZi = salariuPeZi; }
    public Manager getManager() { return manager; }
    public void setManager(Manager manager) { this.manager = manager; }
}