package guestbook.repository;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import guestbook.repository.template.JdbcContext;
import guestbook.vo.GuestbookVo;

@Repository
public class GuestbookRepository {
	private JdbcContext jdbcContext;
	//private DataSource dataSource; // JdbcContext에서 이미 주입 받았기에 주입받을 필요 x
	
	public GuestbookRepository(JdbcContext jdbcContext) {
		this.jdbcContext = jdbcContext;
	}
	
	public List<GuestbookVo> findAll() {
		return jdbcContext.query(
			"select id, name, contents, date_format(reg_date, '%Y-%m-%d %h:%i:%s') as regDate from guestbook order by reg_date desc",
			new BeanPropertyRowMapper<>(GuestbookVo.class)); 
	}

	public int insert(GuestbookVo vo) {
		return jdbcContext.update(
			"insert into guestbook values(null, ?, ?, ?, now())", vo.getName(), vo.getPassword(), vo.getContents());
	}

	public int deleteByIdAndPassword(Long id, String password) {
		return jdbcContext.update(
			"delete from guestbook where id=? and password=?", id, password);
	}

	public GuestbookVo findById(Long id) {
		return jdbcContext.queryForObject(
			"select id, name, contents, date_format(reg_date, '%Y-%m-%d') as regDate from guestbook where id=?",
			new Object[] {id},
			new BeanPropertyRowMapper<>(GuestbookVo.class));
	}
}
