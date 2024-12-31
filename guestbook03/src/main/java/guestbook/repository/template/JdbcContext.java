package guestbook.repository.template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

public class JdbcContext {
	
	private DataSource dataSource;
	public JdbcContext(DataSource dataSource) {
		this.dataSource = dataSource;
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
}
