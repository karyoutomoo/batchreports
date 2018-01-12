package batchreports.step;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import batchreports.model.dao.TransactionDAO;
import batchreports.model.entity.Transaction;

public class Writer implements ItemWriter<Transaction> {
 
	private final TransactionDAO transactionDao;
	
	public Writer(TransactionDAO transactionDao) {
	this.transactionDao=transactionDao;
	}
	
	@Override
	public void write(List<? extends Transaction> transactions) throws Exception {
		transactionDao.insert(transactions);
	}
}