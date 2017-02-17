package com.itcast.demo;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;

import org.junit.Test;

import com.itcast.jdbc.utils.JdbcUtils;

public class Demo2 {
	
	//发送不同的sql语句
	@Test
	public void test1() {
		Connection connection=null;
		Statement st=null;
		ResultSet rs=null;
		
		
		try {
			connection=JdbcUtils.getConnection();
			st=connection.createStatement();
			String sql1="insert into testbatch (id,name) values ('1','aaa')";
			String sql2="update testbatch set name='bbb' ,id=1";
			st.addBatch(sql1);
			st.addBatch(sql2);
			st.executeBatch();
			st.clearBatch();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JdbcUtils.release(rs, st, connection);
		}
	}
	
	//批量插入,更新
	@Test
	public void test2() {
		Connection connection=null;
		PreparedStatement st=null;
		ResultSet rs=null;
		
		try {
			connection=JdbcUtils.getConnection();
			String sql1="insert into testbatch (id,name) values (?,?)";
			st=connection.prepareStatement(sql1);
			st.setString(1, "1");
			st.setString(2, "aaa");
			st.addBatch();
			
			st.setString(1, "2");
			st.setString(2, "bbb");
			st.addBatch();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JdbcUtils.release(rs, st, connection);
		}
	}
	
	@Test
	public void test3() {
		Connection connection=null;
		PreparedStatement st=null;
		ResultSet rs=null;
		
		try {
			connection=JdbcUtils.getConnection();
			String sql1="insert into testbatch (id,name) values (?,?)";
			st=connection.prepareStatement(sql1);
			for(int i=0;i<10000006;i++){
				st.setString(1, i+"");
				st.setString(2, i+"a");
				st.addBatch();
				if (i%1000==0) {//否则内存不足
					st.executeBatch();
					st.clearBatch();
				}
			}
			st.executeBatch();
			st.clearBatch();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JdbcUtils.release(rs, st, connection);
		}
	}
	
	@Test//获取自增主键，只对insert有效
	public void tes4() {
		Connection connection=null;
		PreparedStatement st=null;
		ResultSet rs=null;
		
		try {
			connection=JdbcUtils.getConnection();
			String sql1="insert into testbatch (name) values ('aaa')";
			st=connection.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
			st.executeUpdate();
			rs=st.getGeneratedKeys();
			if (rs.next()) {
				rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JdbcUtils.release(rs, st, connection);
		}
	}
	
	@Test//调用存储过程
	
	public void tes5() {
		Connection connection=null;
		CallableStatement cs=null;
		ResultSet rs=null;
		
		try {
			connection=JdbcUtils.getConnection();
			cs=connection.prepareCall("{call demoPC (? , ?) }");
			cs.setString(1, "abc");
			cs.registerOutParameter(2, Types.VARCHAR);
			cs.execute();
			System.out.println(cs.getString(2));
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JdbcUtils.release(rs, cs, connection);
		}
	}
}
