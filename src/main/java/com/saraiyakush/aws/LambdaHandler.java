package com.saraiyakush.aws;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;

public class LambdaHandler implements RequestHandler<S3Event, String> {

	/*
	 * You want to get access to the Spring managed bean @SampleService
	 * But you cannot simply @Autowire
	 * because the Spring boot configuration is not loaded when Lambda invokes this class
	 */
	SampleService sampleService;

	/*
	 * Initialize the Spring application during the class creation.
	 * 
	 * SpringApplication.run(AwsLambdaSpringBootApplication.class, args) is the same call that happens in
	 * @SpringBootApplication class main method
	 */
	public LambdaHandler() {
		// This will load the Spring application
		ConfigurableApplicationContext ctx = SpringApplication.run(AwsLambdaSpringBootApplication.class, new String[] {});
		
		// Initialize the dependencies using the spring context
		sampleService = ctx.getBean(SampleService.class);
	}
	
	@Override
	public String handleRequest(S3Event input, Context context) {
		
		/*
		 * The above Spring Application can be loaded within this method too i.e. Lambda handler too
		 * instead of inside class constructor
		 * But doing so will load your Spring application each time.
		 * 
		 * Loading the Spring application outside of this method gives AWS Lambda an opportunity
		 * to reuse the application in case AWS uses the same execution context to make subsequent
		 * Lambda invocations
		 */
		
		// Invoke the intended method
		sampleService.someDatabaseCall();
		return "SUCCESS";
	}
}
