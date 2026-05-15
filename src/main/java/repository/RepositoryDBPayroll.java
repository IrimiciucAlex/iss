package repository;

import domain.Payroll;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import java.util.UUID;

public class RepositoryDBPayroll implements Repository<UUID, Payroll> {

    @Override
    public void save(Payroll entity) {
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
            Payroll p = session.get(Payroll.class, id);
            if (p != null) session.remove(p);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public void update(Payroll entity) {
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
    public Payroll findById(UUID id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(Payroll.class, id);
        }
    }

    @Override
    public List<Payroll> getAll() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Payroll", Payroll.class).list();
        }
    }
}