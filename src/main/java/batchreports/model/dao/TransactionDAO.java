package batchreports.model.dao;

import java.util.List;

import batchreports.model.entity.Transaction;

public interface TransactionDAO {
	public void insert(List<? extends Transaction> transactions);
	List<Transaction> loadAllTransactions();
}
