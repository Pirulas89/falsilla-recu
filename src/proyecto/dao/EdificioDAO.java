package proyecto.dao;

import proyecto.modelo.Edificio;
import proyecto.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class EdificioDAO {

    public void insertar(Edificio e) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(e);
            tx.commit();
        }
    }

    public void actualizar(Edificio e) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(e);
            tx.commit();
        }
    }

    public void eliminar(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Edificio ed = session.get(Edificio.class, id);
            session.remove(ed);
            tx.commit();
        }
    }

    public Edificio obtenerPorId(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Edificio.class, id);
        }
    }

    public List<Edificio> obtenerTodos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Edificio", Edificio.class).list();
        }
    }
}
