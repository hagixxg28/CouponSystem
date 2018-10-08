package f.dao;

import java.util.Collection;

import d.beanShells.Coupon;
import d.beanShells.Customer;

public interface CustomerDao {
	void createCustomer(Customer cust);

	void removeCustomer(Customer cust);

	void updateCustomer(Customer cust);

	Customer getCustomer(Customer cust);

	Collection<Customer> getAllCustomer();

	Boolean login(Long id, String password);

	Collection<Coupon> getCoupons(Customer cust);

}
