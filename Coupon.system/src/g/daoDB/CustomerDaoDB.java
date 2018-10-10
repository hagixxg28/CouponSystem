package g.daoDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import c.connectionPool.ConnectionPool;
import d.beanShells.Coupon;
import d.beanShells.Customer;
import e.enums.CouponType;
import f.dao.CustomerDao;

public class CustomerDaoDB implements CustomerDao {

	public CustomerDaoDB() {
		super();
	}

	@Override
	public void createCustomer(Customer cust) {
		ConnectionPool pool = ConnectionPool.getPool();
		String sql = "INSERT INTO customer  (cust_id,name,password) VALUES (?,?,?)";
		Connection con = pool.getConnection();
		try (PreparedStatement stmt = con.prepareStatement(sql);) {
			stmt.setLong(1, cust.getId());
			stmt.setString(2, cust.getCustName());
			stmt.setString(3, cust.getPassword());
			stmt.executeUpdate();
			System.out.println(cust + " has been added");
		} catch (SQLException e) {
			System.err.println(sql);
		} finally {
			pool.returnConnection(con);
		}

	}

	@Override
	public void removeCustomer(Customer cust) {
		ConnectionPool pool = ConnectionPool.getPool();
		String sql = String.format("DELETE FROM customer WHERE cust_id=%d", cust.getId());
		Connection con = pool.getConnection();
		try (PreparedStatement stmt = con.prepareStatement(sql);) {
			stmt.executeUpdate();
			System.out.println(cust + " was removed");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(con);
		}

	}

	@Override
	public void updateCustomer(Customer cust) {
		ConnectionPool pool = ConnectionPool.getPool();
		String sql = "UPDATE customer SET name=?, password=? WHERE cust_id=?";
		Connection con = pool.getConnection();
		try (PreparedStatement stmt = con.prepareStatement(sql);) {
			stmt.setString(1, cust.getCustName());
			stmt.setString(2, cust.getPassword());
			stmt.setLong(3, cust.getId());
			stmt.executeUpdate();
			System.out.println(cust + " was updated");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(con);
		}

	}

	@Override
	public Customer getCustomer(Customer cust) {
		ConnectionPool pool = ConnectionPool.getPool();
		Customer tempCust = new Customer();
		String sql = String.format("SELECT * FROM customer WHERE cust_id=%d", cust.getId());
		Connection con = pool.getConnection();
		try (PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery();) {
			while (rs.next()) {
				tempCust.setId(rs.getLong("cust_id"));
				tempCust.setCustName(rs.getString("name"));
				tempCust.setPassword(rs.getString("password"));
				tempCust.setCoupons(getCoupons(cust));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			pool.returnConnection(con);
		}
		return tempCust;

	}

	@Override
	public Collection<Customer> getAllCustomer() {
		ConnectionPool pool = ConnectionPool.getPool();
		Collection<Customer> collection = new ArrayList<Customer>();
		String sql = "SELECT * FROM customer";
		Connection con = pool.getConnection();
		try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql);) {
			while (rs.next()) {
				Customer other = new Customer(rs.getLong("cust_id"), rs.getString("name"), rs.getString("password"));
				collection.add(other);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(con);
		}
		return collection;

	}

	public Collection<Customer> getAllCustomerWithCoupons() {
		ConnectionPool pool = ConnectionPool.getPool();
		Collection<Customer> collection = new ArrayList<Customer>();
		String sql = "SELECT * FROM customer";
		Connection con = pool.getConnection();
		try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql);) {
			while (rs.next()) {
				Customer other = new Customer(rs.getLong("cust_id"), rs.getString("name"), rs.getString("password"));
				other.setCoupons(getCoupons(other));
				collection.add(other);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(con);
		}
		return collection;

	}

	@Override
	public Boolean login(Long id, String password) {
		ConnectionPool pool = ConnectionPool.getPool();
		String sql = "SELECT password FROM customer WHERE cust_id=" + id;
		Connection con = pool.getConnection();
		try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql);) {
			rs.next();
			String str = rs.getString("password");
			if (password.equals(str)) {
				return true;
			} else {
				System.out.println("Wrong pass");
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(con);
		}
		System.out.println("I'm bad");
		return false;
	}

	@Override
	public Collection<Coupon> getCoupons(Customer cust) {
		ConnectionPool pool = ConnectionPool.getPool();
		Collection<Coupon> collection = new ArrayList<Coupon>();
		String sql = "SELECT customer_coupon.coup_id FROM customer_coupon INNER JOIN coupon"
				+ " ON coupon.coup_id=customer_coupon.coup_id" + " WHERE cust_id=" + cust.getId();
		Connection con = pool.getConnection();
		try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql);) {
			while (rs.next()) {
				Coupon other = new Coupon();
				other.setId(rs.getLong("coup_id"));
				collection.add(other);
			}
			for (Coupon coupon : collection) {
				Long coup_id = coupon.getId();
				String sql2 = "Select *  FROM coupon where coup_id=" + coup_id;
				try (ResultSet rss = stmt.executeQuery(sql2);) {
					while (rss.next()) {
						coupon.setId(rss.getLong("coup_id"));
						coupon.setTitle(rss.getString("title"));
						coupon.setStartDate(rss.getDate("start_date"));
						coupon.setEndDate(rss.getDate("end_date"));
						coupon.setAmount(rss.getInt("amount"));
						coupon.setType(CouponType.typeSort(rss.getString("type")));
						coupon.setMessage(rss.getString("message"));
						coupon.setPrice(rss.getDouble("price"));
						coupon.setImage(rss.getString("image"));
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(con);
		}
		return collection;
	}

	public boolean customerExists(Customer cust) {
		ConnectionPool pool = ConnectionPool.getPool();
		ArrayList<Long> list = new ArrayList<>();
		String sql = "SELECT cust_id FROM customer WHERE cust_id=" + cust.getId();
		Connection con = pool.getConnection();
		try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql);) {
			while (rs.next()) {
				list.add((rs.getLong("cust_id")));
			}
			for (Long long1 : list) {
				if (cust.getId() == long1) {
					return true;
				}
			}
		} catch (SQLException e) {
		} finally {
			pool.returnConnection(con);
		}
		return false;
	}
}
