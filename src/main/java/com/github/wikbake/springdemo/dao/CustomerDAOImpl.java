package com.github.wikbake.springdemo.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.github.wikbake.springdemo.entity.Customer;

@Repository
public class CustomerDAOImpl implements CustomerDAO {
	
	// here we talk with Hibernate

	// need to inject the session factory 
	@Autowired
	private SessionFactory sessionFactory; 
	
	@Override
	public List<Customer> getCustomers() {
		
		// get the current Hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// create a query - AND sort by last name
		Query<Customer> theQuery = currentSession.createQuery("from Customer order by lastName", Customer.class);
		
		// execute query and get result list 
		List<Customer> customers = theQuery.getResultList();
		
		// return the results
		return customers;
	}

	@Override
	public void saveCustomer(Customer theCustomer) {
		
		// get current Hibernate session 
		Session currentSesion = sessionFactory.getCurrentSession();
		
		// Knows what to do because of if(primary key/id) empty then insert ELSE update
		// save/update the customer 
		currentSesion.saveOrUpdate(theCustomer);
		
	}

	@Override
	public Customer getCustomer(int theId) {
		
		// get the current Hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// retrieve/read from database using the priamry key
		Customer theCustomer = currentSession.get(Customer.class, theId);
		
		return theCustomer;
	}

	@Override
	public void deleteCustomer(int theId) {

		// get the current Hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// delete object with the primary key 
		Query theQuery = currentSession.createQuery("delete from Customer where id=:customerId");
		
		theQuery.setParameter("customerId", theId);
		
		theQuery.executeUpdate();
		
	}

	@Override
	public List<Customer> searchCustomers(String theSearchName) {
		
		// get the current Hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		Query theQuery = null;
		
		// 
		// only search by name if theSeachName is not empty 
		// 
		if(theSearchName != null && theSearchName.trim().length() > 0) {
			theQuery = currentSession
					.createQuery("from Customer where lower(firstName) like :theName or lower(lastName) like :theName", 
							Customer.class);
			theQuery.setParameter("theName", "%" + theSearchName.toLowerCase() + "%");
		} else {
			// theSearchName is empty so we are getting all customers 
			theQuery = currentSession.createQuery("from Customer", Customer.class);
		}
		
		// execute query and get the result list 
		List<Customer> customers = theQuery.getResultList();
		
		return customers;
	}

}
