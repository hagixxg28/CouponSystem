package j.facade;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;

import b.exceptions.CouponAlreadyExistsException;
import b.exceptions.CouponSystemException;
import b.exceptions.NoCouponsException;
import d.beanShells.Company;
import d.beanShells.Coupon;
import d.beanShells.Customer;
import e.enums.CouponType;
import g.daoDB.CompanyDaoDB;
import g.daoDB.CouponDaoDB;
import g.daoDB.CustomerDaoDB;

public class CompanyFacade implements CouponClientFacade {
	private CompanyDaoDB db = new CompanyDaoDB();
	private Company currentComp = new Company();
	private CouponDaoDB coupDb = new CouponDaoDB();
	private CustomerDaoDB custDb = new CustomerDaoDB();

	public CompanyFacade() {

	}

	public void createCoupon(Coupon coup) throws CouponSystemException {
		Collection<Coupon> List = db.getAllCoupons(currentComp);
		for (Coupon coupon : List) {
			if (coupon.getTitle().equals(coup.getTitle())) {
				throw new CouponAlreadyExistsException("A coupon with this title already exists");
			}
		}
		coupDb.createCoupon(coup, currentComp.getId());
		List.add(coup);
		Company comp = db.readCompany(currentComp);
		comp.setCoupons(List);
	}


	public void custLogin(Long id, String password) {
		if (db.login(id, password)) {
			currentComp.setId(id);
		} else {
			System.out.println("Logging failed");
		}
	}

	public void removeCoupon(Coupon coup) throws CouponSystemException {
		if (coupDb.getCoupon(coup) != null) {
			coupDb.removeCoupon(coup);
			Company comp = db.readCompany(currentComp);
			comp.getCoupons().remove(coup);
			Collection<Customer> List = custDb.getAllCustomer();
			for (Customer customer : List) {
				if (customer.getCoupons().contains(coup)) {
					customer.getCoupons().remove(coup);
				}
			}
		} else {
			throw new NoCouponsException("There is no such coupon in the Database");
		}
	}

	public void updateCoupon(Coupon coup) throws CouponSystemException {
		if (coupDb.getCoupon(coup) != null) {
			Double newPrice = coup.getPrice();
			Date newDate = coup.getEndDate();
			Coupon updatedCoup = coupDb.getCoupon(coup);
			updatedCoup.setPrice(newPrice);
			updatedCoup.setEndDate(newDate);
			coupDb.updateCoupon(updatedCoup);
		} else {
			throw new NoCouponsException("There is no such coupon in the Database");
		}
	}

	public Coupon getCoupon(Coupon coup) {
		return coupDb.getCoupon(coup);
	}

	public Collection<Coupon> getAllCoupon() {
		Company comp = db.readCompany(currentComp);
		return comp.getCoupons();
	}

	public Collection<Coupon> getAllCouponByType(CouponType type) {
		Company comp = db.readCompany(currentComp);
		Collection<Coupon> List = comp.getCoupons();
		Collection<Coupon> TypeList = new ArrayList<>();
		for (Coupon coupon : List) {
			if (coupon.getType().equals(type)) {
				TypeList.add(coupon);
			}
		}
		return TypeList;
	}

	public Collection<Coupon> getAllCouponByPrice(double price) {
		Company comp = db.readCompany(currentComp);
		Collection<Coupon> List = comp.getCoupons();
		Collection<Coupon> priceList = new ArrayList<>();
		for (Coupon coupon : List) {
			if (coupon.getPrice() <= price) {
				priceList.add(coupon);
			}
		}
		return priceList;
	}

	public Collection<Coupon> getAllCouponByDate(Date date) {
		Company comp = db.readCompany(currentComp);
		Collection<Coupon> List = comp.getCoupons();
		Collection<Coupon> dateList = new ArrayList<>();
		for (Coupon coupon : List) {
			if (coupon.getEndDate().before(date)) {
				dateList.add(coupon);
			}
		}
		return dateList;
	}
}
