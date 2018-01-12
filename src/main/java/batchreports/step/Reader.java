package batchreports.step;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;

import batchreports.model.entity.Transaction;

public class Reader {
	public static FlatFileItemReader<Transaction> reader(String path) {
		FlatFileItemReader<Transaction> reader = new FlatFileItemReader<Transaction>();
 
		reader.setResource(new ClassPathResource(path));
		reader.setLineMapper(new DefaultLineMapper<Transaction>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						//setNames(new String[] { "id", "firstName", "lastName" });
						setNames(new String[] {"id","buyer","store","item","price"});
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<Transaction>() {
					{
						setTargetType(Transaction.class);
					}
				});
			}
		});
		return reader;
	}
}
