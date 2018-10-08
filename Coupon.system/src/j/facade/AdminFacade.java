package j.facade;

import java.util.Collection;

import b.exceptions.CompanyAlreadyExistsException;
import b.exceptions.CompanyDoesNotExistException;
import b.exceptions.CouponSystemException;
import b.exceptions.CustomerAlreadyExistsException;
import b.exceptions.CustomerDoesNotExistException;
import b.exceptions.NoCompaniesException;
import b.exceptions.NoCustomersException;
import d.beanShells.Company;
import d.beanShells.Coupon;
import d.beanShells.Customer;
import g.daoDB.CompanyDaoDB;
import g.daoDB.CouponDaoDB;
import g.daoDB.CustomerDaoDB;

public class AdminFacade implements CouponClientFacade {

	private CustomerDaoDB custDb = new CustomerDaoDB();
	private CompanyDaoDB compDb = new CompanyDaoDB();
	private CouponDaoDB coupDb = new CouponDaoDB();

	public AdminFacade() {

	}

	public void createCompany(Company comp) throws CompanyAlreadyExistsException {
		if (!compDb.companyExists(comp)) {
			compDb.createCompany(comp);
		} else {
			throw new CompanyAlreadyExistsException("A company with this name already exists");
		}
	}

	public void removeCompany(Company comp) throws CompanyDoesNotExistException {
		if (compDb.companyExists(comp)) {
			compDb.removeCompany(comp);
			for (Coupon coup : compDb.getAllCoupons(comp)) {
				coupDb.removeCoupon(coup);
			}
		} else {
			throw new CompanyDoesNotExistException("This company dosen't exist");
		}
	}

	public void updateCompany(Company comp) throws CompanyDoesNotExistException {
		if (compDb.companyExists(comp)) {
			compDb.updateCompany(comp);
		} else {
			throw new CompanyDoesNotExistException("This company dosen't exist");
		}
	}

	public Company getCompany(Company comp) throws CompanyDoesNotExistException {
		if (compDb.readCompany(comp) != null) {
			return compDb.readCompany(comp);
		} else {
			throw new CompanyDoesNotExistException("This company dosen't exist");
		}
	}

	public Collection<Company> getAllCompanies() throws NoCompaniesException {
		if (!compDb.getAllCompanies().isEmpty()) {
			return compDb.getAllCompanies();
		} else {
			throw new NoCompaniesException("There are no companies on the database");
		}
	}

	public void createCustomer(Customer cust) throws CustomerAlreadyExistsException {
		if (!custDb.customerExists(cust)) {
			custDb.createCustomer(cust);
		} else {
			throw new CustomerAlreadyExistsException("A customer with this id already exists");
		}
	}

	public void removeCustomer(Customer cust) throws CouponSystemException {
		if (custDb.customerExists(cust)) {
			custDb.removeCustomer(cust);
			if (!cust.getCouponHistory().isEmpty()) {
				cust.getCouponHistory().clear();
			}
		} else {
			throw new CustomerDoesNotExistException("This customer does not exist");
		}

	}

	public void updateCustomer(Customer cust) throws CustomerDoesNotExistException {
		if (custDb.customerExists(cust)) {
			custDb.updateCustomer(cust);
		} else {
			throw new CustomerDoesNotExistException("This customer does not exist");
		}
	}

	public Customer getCustomer(Customer cust) throws CustomerDoesNotExistException {
		if (custDb.getCustomer(cust) != null) {
			return custDb.getCustomer(cust);
		} else {
			throw new CustomerDoesNotExistException("This customer does not exist");
		}
	}

	public Collection<Customer> getAllCustomer() throws NoCustomersException {
		if (!custDb.getAllCustomer().isEmpty()) {
			return custDb.getAllCustomer();
		} else {
			throw new NoCustomersException("there are no customers on the database");
		}
	}
}
