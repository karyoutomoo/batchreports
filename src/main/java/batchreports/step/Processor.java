package batchreports.step;


import java.util.Random;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import batchreports.model.entity.Transaction;
 
 
public class Processor implements ItemProcessor<Transaction, Transaction> {
 
	private static final Logger log = LoggerFactory.getLogger(Processor.class);
 
	@Override
	public Transaction process(Transaction transaction) throws Exception {
		Random r = new Random();
		final String buyer = transaction.getBuyer().toUpperCase();
		final String store = transaction.getStore().toUpperCase();
		final String item = transaction.getItem().toUpperCase();
 
		final Transaction fixedTransaction = new Transaction(r.nextLong(), buyer, store, item);
 
		log.info("Converting (" + transaction + ") into (" + fixedTransaction + ")");
 
		return fixedTransaction;
	}
}