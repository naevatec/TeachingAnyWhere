package com.naevatec.taw.backend;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.naevatec.taw.backend.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

//ONLY ON PRODUCTION
//ONLY ON PRODUCTION 

@SpringBootApplication
@EnableWebSocket
public class Application
{
    public static void main( String[] args )
    {
    	SpringApplication.run(Application.class, args);
    }
    

	@Bean
	public AuthorizationService authorizationService() {
		return new AuthorizationService();
	}


    //ONLY ON PRODUCTION
    @Value("${aws_access_key_id}")
    private String awsId;
 	
    @Value("${aws_secret_access_key}")
    private String awsKey;
    
    @Bean
    public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
	 	PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
	 	ppc.setLocations(new Resource[] {
	 		new ClassPathResource("/general.properties")
	         });
	 	return ppc;
    }
    
    @Bean
    public AWSCredentials credential() {
    	return new BasicAWSCredentials(awsId, awsKey);
    }
    
    @Bean
    public AmazonS3 s3client() {
    	return new AmazonS3Client(credential()); 
    }
    //ONLY ON PRODUCTION
    

}
