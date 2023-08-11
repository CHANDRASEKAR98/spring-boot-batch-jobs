package com.project.springbatch.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is an entity of customer_info table
 * @author chandrasekar
 *
 */
@Entity
@Table(name = "CUSTOMER_INFO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
	
	@Id
	@Column(name = "customer_id")
	private Long customerId;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "email_id")
	private String emailId;
	
	@Column(name = "contact_no")
	private String contactNo;

}
