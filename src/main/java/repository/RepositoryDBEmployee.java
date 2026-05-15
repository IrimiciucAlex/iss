package repository;

import domain.Employee;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import java.util.UUID;

public class RepositoryDBEmployee implements Repository<UUID, Employee> {

    @Override
    public void save(Employee entity) {
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public void delete(UUID id) {
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Employee emp = session.get(Employee.class, id);
            if (emp != null) session.remove(emp);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public void update(Employee entity) {
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(entity);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public Employee findById(UUID id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(Employee.class, id);
        }
    }

    @Override
    public List<Employee> getAll() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Employee", Employee.class).list();
        }
    }

    // Metoda findByCriteria din diagrama de secventa "ViewEmployees"
    public List<Employee> findByCriteria(String criteria, String value) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            // HQL dinamic in functie de criteriu (ex: nume sau functie)
            String hql = "from Employee e where e." + criteria + " like :val";
            return session.createQuery(hql, Employee.class)
                    .setParameter("val", "%" + value + "%")
                    .list();
        }
    }
}