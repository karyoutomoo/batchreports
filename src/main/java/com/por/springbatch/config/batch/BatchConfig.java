package com.por.springbatch.config.batch;

import com.por.springbatch.config.BatchListener;
import com.por.springbatch.config.StringHeaderWriter;
import com.por.springbatch.model.Data;
import com.por.springbatch.processor.InputProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;
import java.sql.ResultSet;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchConfig.class);
    private static final String PROPERTY_CSV_EXPORT_FILE_HEADER = "database.to.csv.job.export.file.header";
    private static final String PROPERTY_CSV_EXPORT_FILE_PATH = "database.to.csv.job.export.file.path";

    /*
    * ItemReader ini bertugas untuk membaca data dari database.
    * SetSQL itu select [columns] from [table]
    * SetDataSource itu dari class DbConfig
    * */
    @Bean
    public ItemReader<Data> reader(DataSource dataSource) {
        JdbcCursorItemReader<Data> reader = new JdbcCursorItemReader<Data>();
        reader.setSql("select id, name from data");
        reader.setDataSource(dataSource);
        reader.setRowMapper(
                (ResultSet resultSet, int rowNum) -> {
                    LOGGER.info("RowMapper resultset: {}", resultSet);
                    if (!(resultSet.isAfterLast()) && !(resultSet.isBeforeFirst())) {
                        Data input = new Data();
                        input.setId(resultSet.getLong("id"));
                        input.setName(resultSet.getString("name"));

                        LOGGER.info("RowMapper record : {}", input);
                        return input;
                    } else {
                        LOGGER.info("Returning null from rowMapper");
                        return null;
                    }
                });
        return reader;
    }

    @Bean
    public ItemProcessor<Data, Data> processor() {
        return new InputProcessor();
    }

    @Bean
    public ItemPreparedStatementSetter<Data> setter() {
        return (item, ps) -> {
            ps.setLong(1, item.getId());
            ps.setString(2, item.getName());
        };
    }

    @Bean
    public ItemWriter<Data> writer(Environment environment) {
        FlatFileItemWriter<Data> fileItemWriter = new FlatFileItemWriter<>();
        String exportFileHeader = environment.getRequiredProperty(PROPERTY_CSV_EXPORT_FILE_HEADER);
        StringHeaderWriter headerWriter = new StringHeaderWriter(exportFileHeader);
        fileItemWriter.setHeaderCallback(headerWriter);

        String exportFilePath = environment.getRequiredProperty(PROPERTY_CSV_EXPORT_FILE_PATH);
        fileItemWriter.setResource(new FileSystemResource(exportFilePath));

        LineAggregator<Data> lineAggregator = createDataLineAggregator();
        fileItemWriter.setLineAggregator(lineAggregator);

        return fileItemWriter;
    }

    private LineAggregator<Data> createDataLineAggregator() {
        DelimitedLineAggregator<Data> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(";");

        FieldExtractor<Data> fieldExtractor = createDataFieldExtractor();
        lineAggregator.setFieldExtractor(fieldExtractor);

        return lineAggregator;
    }

    private FieldExtractor<Data> createDataFieldExtractor() {
        BeanWrapperFieldExtractor<Data> extractor = new BeanWrapperFieldExtractor<>();
        extractor.setNames(new String[] {"id", "name"});
        return extractor;
    }

    @Bean
    Step databaseToCsvFileStep(ItemReader<Data> databaseCsvItemReader,
                               ItemProcessor<Data, Data> databaseCsvItemProcessor,
                               ItemWriter<Data> databaseCsvItemWriter,
                               StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("databaseToCsvFileStep")
                .<Data, Data>chunk(1000)
                .reader(databaseCsvItemReader)
                .processor(databaseCsvItemProcessor)
                .writer(databaseCsvItemWriter)
                .build();
    }

    @Bean
    Job databaseToCsvFileJob(JobBuilderFactory jobBuilderFactory,
                             @Qualifier("databaseToCsvFileStep") Step csvStudentStep,
                             BatchListener batchListener) {
        return jobBuilderFactory.get("databaseToCsvFileJob")
                .listener(batchListener)
                .incrementer(new RunIdIncrementer())
                .flow(csvStudentStep)
                .end()
                .build();
    }

}
