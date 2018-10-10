package f.dao;

import java.util.Collection;

import b.exceptions.CouponSystemException;
import d.beanShells.Company;
import d.beanShells.Coupon;

public interface CompanyDao {

	void createCompany(Company comp) throws CouponSystemException;

	void removeCompany(Company comp) throws CouponSystemException;

	void updateCompany(Company comp) throws CouponSystemException;

	Company readCompany(Company comp) throws CouponSystemException;

	Collection<Company> getAllCompanies() throws CouponSystemException;

	Boolean login(Long id, String password) throws CouponSystemException;

	Collection<Coupon> getAllCoupons(Company comp) throws CouponSystemException;

}
