package pojo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarsOfPeople {

	private Integer personId;

	private Integer carId;

	public Integer getPersonId() {
		return personId;
	}

	public Integer getCarId() {
		return carId;
	}

	private CarsOfPeople(ResultSet rs) throws SQLException {
		personId = rs.getObject(1) == null ? null : rs.getInt(1);
		carId = rs.getObject(2) == null ? null : rs.getInt(2);
	}

	public static interface Builder {

		List<CarsOfPeople> select(Connection conn) throws SQLException;

		CarsOfPeople first(Connection conn) throws SQLException;

	}

	private static class BuilderImpl implements Builder {

		private static final String QUERY = "SELECT person_id, car_id FROM public.cars_of_people";

		private String getSQL() {
			return QUERY;
		}

		public List<CarsOfPeople> select(Connection conn) throws SQLException {
			try (PreparedStatement ps = conn.prepareStatement(getSQL())) {
				try (ResultSet rs = ps.executeQuery()) {
					ArrayList<CarsOfPeople> list = new ArrayList<>();
					while (rs.next()) {
						list.add(new CarsOfPeople(rs));
					}
					return list;
				}
			}
		}

		public CarsOfPeople first(Connection conn) throws SQLException {
			try (PreparedStatement ps = conn.prepareStatement(getSQL() + " LIMIT 1")) {
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next())
						return new CarsOfPeople(rs);
					else
						return null;
				}
			}
		}

	}

	public static Builder newBuilder() {
		return new BuilderImpl();
	}

}
