package com.project.springbatch.config;

import org.springframework.batch.item.ItemProcessor;

import com.project.springbatch.entity.Customer;

/**
 * This class is used to write the process logic for the data source from the input
 * @author chandrasekar
 *
 */
public class CustomerProcessor implements ItemProcessor<Customer, Customer> {

	@Override
	public Customer process(Customer item) throws Exception {
		return item;
	}

}
