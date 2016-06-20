package com.ggk.spider.utils.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class InsertFinanceData {
	
	public static void main(String[] args) {
		InsertFinanceData operation = new InsertFinanceData();
		operation.insertData();
	}
	
	public void insertData() {
		Connection con = DBUtil.getConnection();
		PreparedStatement pst = null;
		String sql = "insert into financedata values(?, ?, ?, ?, ?, ?, ?)";
		
		try {
			pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pst.setInt(1, 1);
			pst.setString(2, "1");
			pst.setString(3, "aaa");
			pst.setString(4, "2016年6月13日");
			pst.setString(5, "速动比率");
			pst.setString(6, "1.89");
			pst.setString(7, "财务指标");
			pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(con);
			DBUtil.close(pst);
		}
	}

}
