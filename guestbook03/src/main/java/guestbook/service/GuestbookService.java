package guestbook.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import guestbook.repository.GuestbookLogRepository;
import guestbook.repository.GuestbookRepository;
import guestbook.vo.GuestbookVo;

@Service
public class GuestbookService {
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Autowired
	private DataSource dataSource;
	
	private final GuestbookRepository guestbookRepository;
	private final GuestbookLogRepository guestbookLogRepository; 

	public GuestbookService(GuestbookRepository guestbookRepository, GuestbookLogRepository guestbookLogRepository) {
		this.guestbookRepository = guestbookRepository;
		this.guestbookLogRepository = guestbookLogRepository;
	}
	
	public List<GuestbookVo> getContentsList() {
		List<GuestbookVo> list = guestbookRepository.findAll();
		return list;
	}
	
	public void deleteContents(Long id, String password) {
		// TX:BEGIN ///////
		TransactionStatus txStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
		
		try {
			GuestbookVo vo = guestbookRepository.findById(id);
			if (vo == null) {
				return;
			}
			
			int count = guestbookRepository.deleteByIdAndPassword(id, password);
			
			if (count == 1) {
				guestbookLogRepository.update(vo.getRegDate());
			}
			
			// TX:END(SUCCESS) ///////
			transactionManager.commit(txStatus);
			
		} catch (Throwable e) {
			// TX:END(FAIL) ///////
			transactionManager.rollback(txStatus);
			
			throw new RuntimeException(e);
		}
	}
	
	public void addContents(GuestbookVo vo) {
		// 트랜잭션 동기(Connection) 초기화
		TransactionSynchronizationManager.initSynchronization(); //ThreadLocal 공간 만들어놓음 
		Connection conn = DataSourceUtils.getConnection(dataSource); //dataSource의 커넥션 받아서 ThreadLocal 공간에 넣어줌
		
		try {
			// TX:BEGIN ///////
			conn.setAutoCommit(false);
			
			int count = guestbookLogRepository.update();
			
			if (count == 0) { // 첫 글인 경우 
				guestbookLogRepository.insert();
			}
			
			guestbookRepository.insert(vo);
			
			// TX:END(SUCCESS) ///////
			conn.commit();
		} catch (Throwable e) {
			// TX:END(FAIL) ///////
			try {
				conn.rollback();
			} catch (SQLException ignore) {
			}
			throw new RuntimeException(e); //컨트롤러까지 에러 넘기기 
		} finally {
			// 커넥션 close하면 안 되고 release
			DataSourceUtils.releaseConnection(conn, dataSource);
		}
	}
}
