package proyecto.dao;

import proyecto.modelo.Ordenador;
import proyecto.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class OrdenadorDAO {

    public void insertar(Ordenador o) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(o);
            tx.commit();
        }
    }

    public void actualizar(Ordenador o) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(o);
            tx.commit();
        }
    }

    public void eliminar(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Ordenador o = session.get(Ordenador.class, id);
            session.remove(o);
            tx.commit();
        }
    }

    public Ordenador obtenerPorId(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Ordenador.class, id);
        }
    }

    public List<Ordenador> obtenerTodos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Ordenador", Ordenador.class).list();
        }
    }
}
