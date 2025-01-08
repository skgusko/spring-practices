package guestbook.repository.template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceUtils;

import guestbook.vo.GuestbookVo;

public class JdbcContext {
	
	private DataSource dataSource;
	public JdbcContext(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public <E> List<E> query(String sql, RowMapper<E> rowMapper) {
		return queryWithStatementStrategy(new StatementStrategy() {

			@Override
			public PreparedStatement makeStatement(Connection connection) throws SQLException {
				return connection.prepareStatement(sql);
			}
			
		}, rowMapper);
	}
	
	public <E> E queryForObject(String sql, Object[] parameters, RowMapper<E> rowMapper) {
		return queryForObjectWithStatementStrategy(new StatementStrategy() {
			@Override
			public PreparedStatement makeStatement(Connection connection) throws SQLException {
				PreparedStatement pstmt = connection.prepareStatement(sql);
				for (int i = 0; i < parameters.length; i++) {
					pstmt.setObject(i+1, parameters[i]); 
				}
				
				return pstmt;
			}
			
		}, rowMapper);
	}
	

	private <E> List<E> queryWithStatementStrategy(StatementStrategy statementStrategy, RowMapper<E> rowMapper) throws RuntimeException {
		List<E> result = new ArrayList<>();
		
		try (
			Connection conn = dataSource.getConnection();
			PreparedStatement pstmt = statementStrategy.makeStatement(conn);
			ResultSet rs = pstmt.executeQuery();
		) {
			while (rs.next()) {
				E e = rowMapper.mapRow(rs, rs.getRow()); //row 한 줄 씩 넣기 (findAll 메서드에서 바인딩될 객체 지정해둠) 
				result.add(e);
			}
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		return result;
	}
	

	private <E> E queryForObjectWithStatementStrategy(StatementStrategy statementStrategy, RowMapper<E> rowMapper) {
		try (
			Connection conn = dataSource.getConnection();
			PreparedStatement pstmt = statementStrategy.makeStatement(conn);
			ResultSet rs = pstmt.executeQuery();
		) {
			if (rs.next()) {
				return rowMapper.mapRow(rs, rs.getRow()); //row 한 줄 씩 넣기 (findAll 메서드에서 바인딩될 객체 지정해둠) 
			}
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		return null;
	}

	// 사용자인 Client가 좀 더 깔끔하게 사용할 수 있도록 아래처럼 랩핑하여 구현 
	public int update(String sql, Object... parameters) {
		return updateWithStatementStrategy(new StatementStrategy() {
			@Override
			public PreparedStatement makeStatement(Connection connection) throws SQLException {
				PreparedStatement pstmt = connection.prepareStatement(sql);
				for (int i = 0; i < parameters.length; i++) {
					pstmt.setObject(i+1, parameters[i]); 
				}
				return pstmt;
			}
		});
	}
	
	private int updateWithStatementStrategy(StatementStrategy statementStrategy) throws RuntimeException { //operation()
		int count = 0;
		
		try (
				Connection conn = DataSourceUtils.getConnection(dataSource); //ThreadLocal에서 커넥션 가져오기 
				PreparedStatement pstmt = statementStrategy.makeStatement(conn);
		) {
			count = pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return count;
	}

	

	
//	private int executeUpdateWithStatementStrategy(StatementStrategy statementStrategy) { //operation()
//		int count = 0;
//		
//		try (
//				Connection conn = dataSource.getConnection();
//				PreparedStatement pstmt = statementStrategy.makeStatement(conn);
//		) {
//			count = pstmt.executeUpdate();
//		} catch (SQLException e) {
//			System.out.println("error: " + e);
//		}
//		return count;
//	}
}
