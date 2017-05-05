package com.futanari.components;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;

public class LongDateSecondsTypeHandler implements TypeHandler<Long> {

	@Override
	public Long getResult(ResultSet rs, String columnName) throws SQLException {
		Timestamp columnValue = rs.getTimestamp(columnName);
		return this.getLong(columnValue);
	}

	@Override
	public Long getResult(ResultSet rs, int columnIndex) throws SQLException {
		Timestamp columnValue = rs.getTimestamp(columnIndex);
		return this.getLong(columnValue);
	}

	@Override
	public Long getResult(CallableStatement cs, int columnIndex) throws SQLException {
		Timestamp columnValue = cs.getTimestamp(columnIndex);
		return this.getLong(columnValue);
	}

	@Override
	public void setParameter(PreparedStatement ps, int i, Long parameter, JdbcType jdbcType) throws SQLException {
		ps.setTimestamp(i, new Timestamp(parameter * 1000));
	}

	private Long getLong(Timestamp columnValue) {
		if (columnValue == null) {
			return null;
		}
		return columnValue.getTime() / 1000;
	}
}
