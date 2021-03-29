package pojo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonData {

	private Integer personId;

	private String name;

	private Date dataOfBirth;

	private String country;

	private Integer languageId;
	
	public Integer getPersonId() {
		return personId;
	}

	public String getName() {
		return name;
	}

	public Date getDataOfBirth() {
		return dataOfBirth;
	}

	public String getCountry() {
		return country;
	}

	public Integer getLanguageId() {
		return languageId;
	}

	private PersonData(ResultSet rs) throws SQLException {
		personId = rs.getObject(1) == null ? null : rs.getInt(1);
		name = rs.getString(2);
		dataOfBirth = rs.getDate(3);
		country = rs.getString(4);
		languageId = rs.getObject(5) == null ? null : rs.getInt(5);
	}

	public static interface Builder {

		List<PersonData> select(Connection conn) throws SQLException;

		PersonData first(Connection conn) throws SQLException;

	}

	private static class BuilderImpl implements Builder {

		private static final String QUERY = "SELECT person_id, name, data_of_birth, country, language_id FROM public.person_data";

		private String getSQL() {
			return QUERY;
		}

		public List<PersonData> select(Connection conn) throws SQLException {
			try (PreparedStatement ps = conn.prepareStatement(getSQL())) {
				try (ResultSet rs = ps.executeQuery()) {
					ArrayList<PersonData> list = new ArrayList<>();
					while (rs.next()) {
						list.add(new PersonData(rs));
					}
					return list;
				}
			}
		}

		public PersonData first(Connection conn) throws SQLException {
			try (PreparedStatement ps = conn.prepareStatement(getSQL() + " LIMIT 1")) {
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next())
						return new PersonData(rs);
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
