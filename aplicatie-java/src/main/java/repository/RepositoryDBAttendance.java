package repository;

import domain.Attendance;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import java.util.UUID;

public class RepositoryDBAttendance implements Repository<UUID, Attendance> {

    @Override
    public void save(Attendance entity) {
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
            Attendance a = session.get(Attendance.class, id);
            if (a != null) session.remove(a);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public void update(Attendance entity) {
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
    public Attendance findById(UUID id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(Attendance.class, id);
        }
    }

    @Override
    public List<Attendance> getAll() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Attendance", Attendance.class).list();
        }
    }
}