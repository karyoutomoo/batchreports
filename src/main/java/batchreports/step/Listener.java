package batchreports.step;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

import batchreports.model.entity.Transaction;
import batchreports.model.dao.TransactionDAO;;

public class Listener extends JobExecutionListenerSupport {
	private static final Logger log = LoggerFactory.getLogger(Listener.class);
	
	private final TransactionDAO transactionDao;

	public Listener(TransactionDAO transactionDao) {
		this.transactionDao = transactionDao;
	}
 
	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("Finish Job! Check the results");
 
			List<Transaction> transactions = transactionDao.loadAllTransactions();
 
			for (Transaction transaction : transactions) {
				log.info("Found <" + transaction + "> in the database.");
			}
		}
	}
}
