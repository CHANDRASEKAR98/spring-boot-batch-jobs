package com.project.springbatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.springbatch.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
