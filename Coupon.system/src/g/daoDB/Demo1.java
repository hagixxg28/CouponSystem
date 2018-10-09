package g.daoDB;

public class Demo1 {

	public static void main(String[] args) {
//		Date date1 = new Date(System.currentTimeMillis());
//		CouponDaoDB coupDb = new CouponDaoDB();
//		CompanyDaoDB compDb = new CompanyDaoDB();
		CustomerDaoDB custDb = new CustomerDaoDB();
		System.out.println(custDb.login((long) 995, "321"));
	}
}
