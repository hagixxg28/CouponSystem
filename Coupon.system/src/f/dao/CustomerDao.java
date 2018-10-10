package f.dao;

import java.util.Collection;

import b.exceptions.CouponSystemException;
import d.beanShells.Coupon;
import d.beanShells.Customer;

public interface CustomerDao {
	void createCustomer(Customer cust) throws CouponSystemException;

	void removeCustomer(Customer cust) throws CouponSystemException;

	void updateCustomer(Customer cust) throws CouponSystemException;

	Customer getCustomer(Customer cust) throws CouponSystemException;

	Collection<Customer> getAllCustomer() throws CouponSystemException;

	Boolean login(Long id, String password) throws CouponSystemException;

	Collection<Coupon> getCoupons(Customer cust) throws CouponSystemException;

}
