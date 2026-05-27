package com.hibernate.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.hibernate.model.Paciente;
import com.hibernate.util.HibernateUtil;

public class PacienteDAO {
	
	public void insertPaciente(Paciente p) {
		Transaction transaction = null;
		try(Session session = HibernateUtil.getSessionFactory().openSession()){
			transaction = session.beginTransaction();
			session.persist(p);
			transaction.commit();
		} catch(Exception e) {
			if(transaction!=null) {
				transaction.rollback();
			}
		}
		
	}
	
	public void updatePaciente(Paciente p) {
		Transaction transaction = null;
		try(Session session = HibernateUtil.getSessionFactory().openSession()){
			transaction = session.beginTransaction();
			session.merge(p);
			transaction.commit();
		} catch(Exception e) {
			if(transaction!=null) {
				transaction.rollback();
			}
		}
		
	}
	
	
	public void deletePaciente(int id) {
		Transaction transaction = null;
		Paciente p = null;
		try(Session session = HibernateUtil.getSessionFactory().openSession()){
			transaction = session.beginTransaction();
			p = session.get(Paciente.class, id);
			session.remove(p);
			transaction.commit();
		} catch(Exception e) {
			if(transaction!=null) {
				transaction.rollback();
			}
		}
		
	}
	
	public Paciente selectPaciente(int id) {
		Transaction transaction = null;
		Paciente p = null;
		try(Session session = HibernateUtil.getSessionFactory().openSession()){
			transaction = session.beginTransaction();
			p = session.get(Paciente.class, id);
			transaction.commit();
		} catch(Exception e) {
			if(transaction!=null) {
				transaction.rollback();
			}
		}
		return p;
	}
	
	public List<Paciente> selectAllPaciente() {
		Transaction transaction = null;
		List<Paciente> pacientes = null;
		try(Session session = HibernateUtil.getSessionFactory().openSession()){
			transaction = session.beginTransaction();
			pacientes = session.createQuery("from Paciente", Paciente.class).getResultList();
			transaction.commit();
		} catch(Exception e) {
			if(transaction!=null) {
				transaction.rollback();
			}
		}
		return pacientes;
	}
	
	
	public List<Paciente> selectAllDiabetico(){
		Transaction transaction = null;
		List<Paciente> diabeticos = null;
		try(Session session = HibernateUtil.getSessionFactory().openSession()){
			transaction = session.beginTransaction();
			diabeticos = session.createQuery("from Paciente WHERE glucosa > 125", Paciente.class).getResultList();
			transaction.commit();
		} catch(Exception e) {
			if(transaction!=null) {
				transaction.rollback();
			}
		}
		return diabeticos;
	}
	
	
	
	
	
	

}
