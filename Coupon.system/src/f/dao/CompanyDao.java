package f.dao;

import java.util.Collection;

import d.beanShells.Company;
import d.beanShells.Coupon;

public interface CompanyDao {

	void createCompany(Company comp);

	void removeCompany(Company comp);

	void updateCompany(Company comp);

	Company readCompany(Company comp);

	Collection<Company> getAllCompanies();

	Boolean login(Long id, String password);

	Collection<Coupon> getAllCoupons(Company comp);

}
