package guestbook.repository.template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;

public class JdbcContext {
	
	private DataSource dataSource;
	public JdbcContext(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public <E> List<E> queryForList(String sql, RowMapper<E> rowMapper) {
		return exequeryForListUpdateWithStatementStrategy(new StatementStrategy() {

			@Override
			public PreparedStatement makeStatement(Connection connection) throws SQLException {
				return connection.prepareStatement(sql);
			}
			
		}, rowMapper);
	}
	
	private <E> List<E> exequeryForListUpdateWithStatementStrategy(StatementStrategy statementStrategy, RowMapper<E> rowMapper) {
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
			System.out.println("error: " + e);
		}
		
		return result;
	}

	// 사용자인 Client가 좀 더 깔끔하게 사용할 수 있도록 아래처럼 랩핑하여 구현 
	public int executeUpdate(String sql, Object[] parameters) {
		return executeUpdateWithStatementStrategy(new StatementStrategy() {
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
	
	private int executeUpdateWithStatementStrategy(StatementStrategy statementStrategy) { //operation()
		int count = 0;
		
		try (
				Connection conn = dataSource.getConnection();
				PreparedStatement pstmt = statementStrategy.makeStatement(conn);
		) {
			count = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("error: " + e);
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
