package batchreports.model.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import batchreports.model.dao.TransactionDAO;
import batchreports.model.entity.Transaction;

@Repository
public class TransactionDAOImpl extends JdbcDaoSupport implements TransactionDAO{

	@Autowired
	DataSource dataSource;
 
	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}
	
	@Override
	public void insert(List<? extends Transaction> Transactions) {
		String sql = "INSERT INTO transactions " + "(id, buyer, store, item, price) VALUES (?, ?, ?, ?, ?)";
		getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Transaction transaction = Transactions.get(i);
				ps.setLong(1, transaction.getId());
				ps.setString(2, transaction.getBuyer());
				ps.setString(3, transaction.getStore());
				ps.setString(3, transaction.getItem());
				ps.setLong(4, transaction.getPrice());
			}
 
			public int getBatchSize() {
				return Transactions.size();
			}
		});
	}

	@Override
	public List<Transaction> loadAllTransactions() {
		String sql = "SELECT * FROM transactions";
		List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql);
 
		List<Transaction> result = new ArrayList<Transaction>();
		for (Map<String, Object> row : rows) {
			Transaction transaction = new Transaction();
			transaction.setId((Long) row.get("id"));
			transaction.setBuyer((String) row.get("buyer"));
			transaction.setStore((String) row.get("store"));
			transaction.setItem((String) row.get("item"));
			transaction.setPrice((Long) row.get("price"));
			result.add(transaction);
		}
 
		return result;
	}

}
