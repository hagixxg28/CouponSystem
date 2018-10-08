package g.daoDB;

import java.sql.Date;

import d.beanShells.Company;
import d.beanShells.Coupon;
import e.enums.CouponType;

public class Demo1 {

	public static void main(String[] args) {
		Date date1 = new Date(System.currentTimeMillis());
		CouponDaoDB coupDb = new CouponDaoDB();
		CompanyDaoDB compDb = new CompanyDaoDB();
		CustomerDaoDB custDb = new CustomerDaoDB();
		System.out.println(custDb.login((long) 995, "321"));
	}
}
