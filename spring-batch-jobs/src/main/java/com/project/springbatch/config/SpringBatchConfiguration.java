package com.project.springbatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.project.springbatch.entity.Customer;
import com.project.springbatch.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

/**
 * This class is used as a configuration for the Spring Batch Process
 * @author chandrasekar
 *
 */
@Configuration
@RequiredArgsConstructor
public class SpringBatchConfiguration {
	
	private final CustomerRepository customerRepository;
	
	/**
	 * This method is used to configure the ItemReader for reading the input from the datasource
	 * @return FlatFileItemReader
	 */
	@Bean
	public FlatFileItemReader<Customer> itemReader() {
		FlatFileItemReader<Customer> itemReader = new FlatFileItemReader<>();
		itemReader.setResource(new FileSystemResource("src/main/resources/static/customers.csv"));
		itemReader.setName("customerReader");
		itemReader.setLinesToSkip(1);
		itemReader.setLineMapper(lineMapper());
		return itemReader;
	}

	/**
	 * This method is used to extract the data from the input datasource and set to field mapper
	 * @return LineMapper
	 */
	private LineMapper<Customer> lineMapper() {
		DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();
		
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames("customerId", "firstName", "lastName", "emailId", "contactNo");
		
		BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(Customer.class);
		
		lineMapper.setFieldSetMapper(fieldSetMapper);
		lineMapper.setLineTokenizer(lineTokenizer);
		return lineMapper;
	}
	
	/**
	 * This method is used to configure the ItemProcessor to process the business logic on the input datasource
	 * @return CustomerProcessor
	 */
	@Bean
	public CustomerProcessor itemProcess() {
		return new CustomerProcessor();
	}
	
	/**
	 * This method is used to configure the ItemWriter to write the data to datasource
	 * @return RepositoryItemWriter
	 */
	@Bean
	public RepositoryItemWriter<Customer> itemWriter() {
		RepositoryItemWriter<Customer> writer = new RepositoryItemWriter<>();
		writer.setRepository(customerRepository);
		writer.setMethodName("save");
		return writer;
	}
	
	/**
	 * This method is used to configure the Step which includes the reader, processor and writer
	 * @param jobRepository
	 * @param transactionManager
	 * @return Step
	 */
	@Bean
	public Step batchStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("csv-step", jobRepository).<Customer, Customer>chunk(10, transactionManager)
				.reader(itemReader())
				.processor(itemProcess())
				.writer(itemWriter())
				.taskExecutor(taskExecutor()).build();
	}
	
	/**
	 * THis method is used to configure the Job which includes the steps involved in it.
	 * @param jobRepository
	 * @param transactionManager
	 * @return Job
	 */
	@Bean
	public Job runBatchJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new JobBuilder("csv-job", jobRepository)
				.flow(batchStep(jobRepository, transactionManager))
				.end().build();
	}
	
	/**
	 * This method is used to asynchronously execute the batch process
	 * @return TaskExecutor
	 */
	@Bean
	public TaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
		asyncTaskExecutor.setConcurrencyLimit(2);
		return asyncTaskExecutor;
	}

}
