package com.futanari.components;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StringArray2TypeHandler implements TypeHandler<String[]> {

	@Override
	public String[] getResult(ResultSet rs, String columnName) throws SQLException {
		String columnValue = rs.getString(columnName);
		return this.getStringArray(columnValue);
	}

	@Override
	public String[] getResult(ResultSet rs, int columnIndex) throws SQLException {
		String columnValue = rs.getString(columnIndex);
		return this.getStringArray(columnValue);
	}

	@Override
	public String[] getResult(CallableStatement cs, int columnIndex) throws SQLException {
		String columnValue = cs.getString(columnIndex);
		return this.getStringArray(columnValue);
	}

	@Override
	public void setParameter(PreparedStatement ps, int i, String[] parameter, JdbcType jdbcType) throws SQLException {
		if (parameter == null)
			ps.setNull(i, Types.VARCHAR);
		else {
			StringBuffer result = new StringBuffer();
			for (String value : parameter) {
				result.append(value).append(" ");
			}
			result.deleteCharAt(result.length() - 1);
			ps.setString(i, result.toString());
		}
	}

	private String[] getStringArray(String columnValue) {
		if (columnValue == null) {
			return null;
		}
		String[] temp = columnValue.split(" ");
		List<String> tag = new ArrayList<String>();
		for (String s : temp) {
			if (!s.equals("") && !s.equals(" ")) {
				tag.add(s);
			}
		}
		temp = new String[tag.size()];
		return tag.toArray(temp);
	}
}
