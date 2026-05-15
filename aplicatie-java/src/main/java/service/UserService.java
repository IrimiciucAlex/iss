package service;

import domain.Manager;
import repository.RepositoryDBManager;
import utils.PasswordUtils;

public class UserService {
    private RepositoryDBManager managerRepo;

    public UserService(RepositoryDBManager managerRepo) {
        this.managerRepo = managerRepo;
    }

    /**
     * Autentifica un manager.
     * @return Manager-ul daca datele sunt corecte, null altfel.
     */
    public Manager authenticate(String username, String password) {
        Manager manager = managerRepo.findByUsername(username);
        if (manager != null) {
            // Verificam parola folosind utilitarul de hash
            if (PasswordUtils.verify(password, manager.getPassword())) {
                return manager;
            }
        }
        return null;
    }
}