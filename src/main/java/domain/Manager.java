package domain;

import jakarta.persistence.*;
import repository.HasId;

import java.util.UUID;

@Entity
@Table(name = "managers")
public class Manager implements HasId<UUID> {
    @Id
    @GeneratedValue
    private UUID id;

    private String username;
    private String password;
    private String nume;
    private String email;

    public Manager() {}

    public Manager(String username, String password, String nume, String email) {
        this.username = username;
        this.password = password;
        this.nume = nume;
        this.email = email;
    }

    @Override
    public UUID getId() { return id; }
    @Override
    public void setId(UUID id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}