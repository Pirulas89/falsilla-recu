package proyecto.dao;

import proyecto.modelo.Cancion;
import proyecto.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CancionDAO {

    public void insertar(Cancion c) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(c);
            tx.commit();
        }
    }

    public void actualizar(Cancion c) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(c);
            tx.commit();
        }
    }

    public void eliminar(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Cancion c = session.get(Cancion.class, id);
            session.remove(c);
            tx.commit();
        }
    }

    public Cancion obtenerPorId(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Cancion.class, id);
        }
    }

    public List<Cancion> obtenerTodas() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Cancion ORDER BY titulo", Cancion.class).list();
        }
    }

    public List<Cancion> obtenerCortas() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Cancion WHERE duracion < 180 ORDER BY titulo", Cancion.class).list();
        }
    }

    public List<Cancion> obtenerLargas() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Cancion WHERE duracion > 180 ORDER BY titulo", Cancion.class).list();
        }
    }
}
