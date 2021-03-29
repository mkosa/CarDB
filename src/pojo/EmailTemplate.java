package pojo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmailTemplate {

	private Integer languageId;

	private String text;

	public Integer getLanguageId() {
		return languageId;
	}

	public String getText() {
		return text;
	}

	private EmailTemplate(ResultSet rs) throws SQLException {
		languageId = rs.getObject(1) == null ? null : rs.getInt(1);
		text = rs.getString(2);
	}

	public static interface Builder {

		List<EmailTemplate> select(Connection conn) throws SQLException;

		EmailTemplate first(Connection conn) throws SQLException;

	}

	private static class BuilderImpl implements Builder {

		private static final String QUERY = "SELECT language_id, text FROM public.email_templates";

		private String getSQL() {
			return QUERY;
		}

		public List<EmailTemplate> select(Connection conn) throws SQLException {
			try (PreparedStatement ps = conn.prepareStatement(getSQL())) {
				try (ResultSet rs = ps.executeQuery()) {
					ArrayList<EmailTemplate> list = new ArrayList<>();
					while (rs.next()) {
						list.add(new EmailTemplate(rs));
					}
					return list;
				}
			}
		}

		public EmailTemplate first(Connection conn) throws SQLException {
			try (PreparedStatement ps = conn.prepareStatement(getSQL() + " LIMIT 1")) {
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next())
						return new EmailTemplate(rs);
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
