package com.homedirect;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableBatchProcessing
public class RestApiApplication {
	
//	private @Autowired JobLauncher jobLauncher;
//	private @Autowired Job job;
	
//	@Scheduled
//	public void AccountItemProcessor() throws Exception {
//		JobParameters parameters = new JobParametersBuilder().addString("JobID", String.valueOf(System.currentTimeMillis())).toJobParameters();
//		jobLauncher.run(job, parameters);
//	}
	
	public static void main(String[] args) {
		SpringApplication.run(RestApiApplication.class, args);
	}
}
