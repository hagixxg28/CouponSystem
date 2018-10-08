package f.dao;

import java.util.Collection;

import d.beanShells.Coupon;
import e.enums.CouponType;

public interface CouponDao {

	void createCoupon(Coupon coup, Long compId);

	void customerPurchaseCoupon(Coupon coup, Long custId);

	void removeCoupon(Coupon coup);

	void updateCoupon(Coupon coup);

	Coupon getCoupon(Coupon coup);

	Collection<Coupon> getAllCoupons();

	Collection<Coupon> getCouponByType(CouponType type);
}