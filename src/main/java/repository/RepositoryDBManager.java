package repository;

import domain.Manager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;
import java.util.UUID;

public class RepositoryDBManager implements Repository<UUID, Manager> {

    @Override
    public void save(Manager entity) {
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
            Manager m = session.get(Manager.class, id);
            if (m != null) session.remove(m);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public void update(Manager entity) {
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
    public Manager findById(UUID id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(Manager.class, id);
        }
    }

    @Override
    public List<Manager> getAll() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Manager", Manager.class).list();
        }
    }

    // Metoda folosita in diagramele de secventa pentru autentificare
    public Manager findByUsername(String username) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Query<Manager> query = session.createQuery("from Manager where username = :u", Manager.class);
            query.setParameter("u", username);
            return query.uniqueResult();
        }
    }
}