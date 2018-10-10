package f.dao;

import java.util.Collection;

import b.exceptions.CouponSystemException;
import d.beanShells.Company;
import d.beanShells.Coupon;
import d.beanShells.Customer;
import e.enums.CouponType;

public interface CouponDao {

	void createCoupon(Coupon coup, Company comp) throws CouponSystemException;

	void customerPurchaseCoupon(Coupon coup, Customer cust) throws CouponSystemException;

	void fullyRemoveCoupon(Coupon coup) throws CouponSystemException;

	void removeCouponComp(Coupon coup) throws CouponSystemException;

	void removeCouponCust(Coupon coup) throws CouponSystemException;

	void removeCouponCoup(Coupon coup) throws CouponSystemException;

	void updateCoupon(Coupon coup) throws CouponSystemException;

	Coupon getCoupon(Coupon coup) throws CouponSystemException;

	Collection<Coupon> getAllCoupons() throws CouponSystemException;

	Collection<Coupon> getCouponByType(CouponType type) throws CouponSystemException;
}