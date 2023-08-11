package com.project.springbatch.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * This class is used to define the Batch process APIs that executes the Job 
 * @author chandrasekar
 *
 */
@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class BatchController {

	private final JobLauncher jobLauncher;
	
	private final Job job;
	
	@PostMapping("/import-customers")
	public String importData() {
		JobParameters jobParameters = new JobParametersBuilder().addLong("start-time", System.currentTimeMillis()).toJobParameters();
		
		try {
			jobLauncher.run(job, jobParameters);
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			e.printStackTrace();
		}
		return "Customer Data is saved";
	}
}
