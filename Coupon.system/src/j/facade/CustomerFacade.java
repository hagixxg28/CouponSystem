package j.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import b.exceptions.CouponAlreadyExistsException;
import b.exceptions.CouponExpiredException;
import b.exceptions.CouponSystemException;
import b.exceptions.InvalidLogging;
import b.exceptions.NoCouponsException;
import b.exceptions.OutOfCouponsException;
import d.beanShells.Coupon;
import d.beanShells.Customer;
import e.enums.CouponType;
import g.daoDB.CouponDaoDB;
import g.daoDB.CustomerDaoDB;

public class CustomerFacade implements CouponClientFacade {
	Date today = new Date(System.currentTimeMillis());
	CustomerDaoDB db = new CustomerDaoDB();
	Customer cust = new Customer();
	CouponDaoDB coupDb = new CouponDaoDB();

	public CustomerFacade() {
	}

	public void custLogin(Long id, String password) throws CouponSystemException {
		if (db.login(id, password)) {
			cust.setId(id);
		} else {
			throw new InvalidLogging("Id or password are incorrect, try again");
		}
	}

	public void purchaseCoupon(Coupon coup) throws CouponSystemException {
		if (coup.getAmount() <= 0) {
			throw new OutOfCouponsException("There are no coupons left to buy");
		}

		if (coup.getEndDate().before(today)) {
			throw new CouponExpiredException("This coupon has expired");
		}
		Customer cust = null;
		cust = db.getCustomer(cust);
		Collection<Coupon> coupons = cust.getCoupons();
		for (Coupon coupon : coupons) {
			if (coupon.getId() == coup.getId()) {
				throw new CouponAlreadyExistsException("You already own this coupon");
			}
		}
		coupDb.customerPurchaseCoupon(coup, cust);
		coupons.add(coup);
		cust.setCoupons(coupons);
		int temp = coup.getAmount();
		temp--;
		coup.setAmount(temp);
		coupDb.updateCoupon(coup);
		db.getCustomer(cust).getCouponHistory().add(coup);
	}

	// This method is not to be used
	public Collection<Coupon> getAllPurchasedCoupons() {
		return db.getCustomer(cust).getCouponHistory();
	}

	public Collection<Coupon> getAllPurchasedCouponsByType(CouponType type) throws CouponSystemException {
		Collection<Coupon> List = new ArrayList<>();
		for (Coupon coupon : db.getCustomer(cust).getCoupons()) {
			if (coupon.getType().equals(type)) {
				List.add(coupon);
			}
		}
		if (!List.isEmpty()) {
			return List;
		} else {
			throw new NoCouponsException("There are no coupons with that type");
		}
	}

	public Collection<Coupon> getAllPurchasedCouponsByPrice(Double price) throws CouponSystemException {
		Collection<Coupon> List = new ArrayList<>();
		for (Coupon coupon : db.getCustomer(cust).getCouponHistory()) {
			if (coupon.getPrice() <= price) {
				List.add(coupon);
			}
		}
		if (!List.isEmpty()) {
			return List;
		} else {
			throw new NoCouponsException("There are no coupns with that price range");
		}
	}

	public Collection<Coupon> getAllCoupons() throws CouponSystemException {
		if (!db.getCoupons(cust).isEmpty()) {
			return db.getCoupons(cust);
		} else {
			throw new NoCouponsException("There are no coupons");
		}
	}
}
