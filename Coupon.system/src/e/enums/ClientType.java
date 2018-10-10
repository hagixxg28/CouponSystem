package e.enums;

public enum ClientType {
	CUSTOMER, COMPANY, ADMIN;
	public static ClientType typeSort(String type) {
		ClientType c = null;
		switch (type) {
		case "customer":
			c = ClientType.CUSTOMER;
			return c;

		case "company":
			c = ClientType.COMPANY;
			return c;

		case "admin":
			c = ClientType.ADMIN;
			return c;

		default:
			c = null;
			return c;
		}
	}
}
