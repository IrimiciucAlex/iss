package repository;

import domain.Bonus;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import java.util.UUID;

public class RepositoryDBBonus implements Repository<UUID, Bonus> {

    @Override
    public void save(Bonus entity) {
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
            Bonus b = session.get(Bonus.class, id);
            if (b != null) {
                session.remove(b);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public void update(Bonus entity) {
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
    public Bonus findById(UUID id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(Bonus.class, id);
        }
    }

    @Override
    public List<Bonus> getAll() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Bonus", Bonus.class).list();
        }
    }
}