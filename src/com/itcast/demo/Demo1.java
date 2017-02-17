package com.itcast.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import com.itcast.jdbc.utils.JdbcUtils;

public class Demo1 {
	
	@Test
	public void insert() {
		Connection connection=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try {
			connection=JdbcUtils.getConnection();
			String sql="insert into testclob (id ,resume) values(?,?)";
			ps=connection.prepareStatement(sql);
			ps.setString(1, "1");
			
			File file=new File("src/1.txt");
			FileReader reader=new FileReader(file);
			ps.setCharacterStream(2,reader,file.length());
			ps.executeUpdate();
		} catch (SQLException | FileNotFoundException e) {
			e.printStackTrace();
		}finally {
			JdbcUtils.release(rs, ps, connection);
		}
	}
	
	@Test
	public void read() {
		Connection connection=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try {
			connection=JdbcUtils.getConnection();
			String sql="select *from testclob where id=?";
			ps=connection.prepareStatement(sql);
			ps.setString(1, "1");
			rs=ps.executeQuery();
			if (rs.next()) {
				Reader reader=rs.getCharacterStream("resume");
				FileWriter writer=new FileWriter("D:\\1.txt");
				int len=0;
				char[] buffer=new char[1024];
				try {
					while ((len=reader.read())!=-1) {
						writer.write(buffer, 0, len);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}finally {
					if (writer!=null) {
						writer.close();
					}
					if (reader!=null) {
						reader.close();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.release(rs, ps, connection);
		}
	}
}
