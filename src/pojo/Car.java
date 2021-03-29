package pojo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Car {

	private Integer carId;

	private String brand;

	private String type;

	private String plateNumber;

	private Integer yearOfManufacture;

	private Integer calculatedValue;

	private Integer drivenDistance;

	private Integer isSent;

	public Integer getCarId() {
		return carId;
	}

	public String getBrand() {
		return brand;
	}

	public String getType() {
		return type;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public Integer getYearOfManufacture() {
		return yearOfManufacture;
	}

	public Integer getCalculatedValue() {
		return calculatedValue;
	}

	public Integer getDrivenDistance() {
		return drivenDistance;
	}

	public Integer getIsSent() {
		return isSent;
	}

	private Car(ResultSet rs) throws SQLException {
		carId = rs.getObject(1) == null ? null : rs.getInt(1);
		brand = rs.getString(2);
		type = rs.getString(3);
		plateNumber = rs.getString(4);
		yearOfManufacture = rs.getObject(5) == null ? null : rs.getInt(5);
		calculatedValue = rs.getObject(6) == null ? null : rs.getInt(6);
		drivenDistance = rs.getObject(7) == null ? null : rs.getInt(7);
		isSent = rs.getObject(8) == null ? null : rs.getInt(8);
	}

	public static interface Builder {

		List<Car> select(Connection conn) throws SQLException;

		Car first(Connection conn) throws SQLException;

	}

	private static class BuilderImpl implements Builder {

		private static final String QUERY = "SELECT car_id, brand, type, plate_number, year_of_manufacture, calculated_value, driven_distance, is_sent FROM public.cars";

		private String getSQL() {
			return QUERY;
		}

		public List<Car> select(Connection conn) throws SQLException {
			try (PreparedStatement ps = conn.prepareStatement(getSQL())) {
				try (ResultSet rs = ps.executeQuery()) {
					ArrayList<Car> list = new ArrayList<>();
					while (rs.next()) {
						list.add(new Car(rs));
					}
					return list;
				}
			}
		}

		public Car first(Connection conn) throws SQLException {
			try (PreparedStatement ps = conn.prepareStatement(getSQL() + " LIMIT 1")) {
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next())
						return new Car(rs);
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
