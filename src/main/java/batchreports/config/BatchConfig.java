package batchreports.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import batchreports.model.dao.TransactionDAO;
import batchreports.model.entity.Transaction;
import batchreports.step.Listener;
import batchreports.step.Processor;
import batchreports.step.Reader;
import batchreports.step.Writer;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
 
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
 
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
 
	@Autowired
	public TransactionDAO transactionDao;
 
	@Bean
	public Job job() {
		return jobBuilderFactory.get("job").incrementer(new RunIdIncrementer()).listener(new Listener(transactionDao))
				.flow(step1()).end().build();
	}
 
	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1").<Transaction, Transaction>chunk(2)
				.reader(Reader.reader("transaction-data.csv"))
				.processor(new Processor()).writer(new Writer(transactionDao)).build();
	}
}