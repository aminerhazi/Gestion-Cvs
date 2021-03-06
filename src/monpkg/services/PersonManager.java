package monpkg.services;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import monpkg.entities.Activity;
import monpkg.entities.Person;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PersonManager {

	@PostConstruct()
	public void debut() {
		System.out.println("Starting " + this);
	}

	@PreDestroy
	public void fin() {
		System.out.println("Stopping " + this);
	}

	@PersistenceContext(unitName = "myMySQLBase")
	EntityManager em;

	public List<Person> findPersons() {
		return em.createQuery("SELECT p FROM Person p", Person.class).getResultList();
	}

	public Person findOnePerson(long idPerson) {
		return em.find(Person.class, idPerson);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void savePerson(Person p) {
		if (em.find(Person.class, p.getIdPerson()) == null) {
			em.persist(p);
		} else {
			em.merge(p);
		}
	}

	public List<Activity> findActivtiesPerson(Person person) {
		Query query = null;
		if (person.getIdPerson() != null) {

			try {
				query = em.createQuery("SELECT a FROM Activity a WHERE a.person.idPerson=" + person.getIdPerson() + "");
			} catch (Exception e) {
			}
			if (query != null) {
				return query.getResultList();
			}
		}
		return null;
	}

}
