package co.hbu.java17springbootconsole;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;


@SpringBootApplication
public class DemoApplication
		implements CommandLineRunner {

	private static Logger LOG = LoggerFactory
			.getLogger(DemoApplication.class);

	@Autowired
	public FailingService failing;


	public static void main(String[] args) {
		LOG.info("STARTING THE APPLICATION");
		SpringApplication.run(DemoApplication.class, args);
		LOG.info("APPLICATION FINISHED");
	}

	@Override
	public void run(String... args) {

		try {
			failing.myFailingMethod();
		} catch (Exception ex) {
			LOG.error("Caught: " + ex.toString(), ex);
		}

	}

	@Configuration
	@ComponentScan(basePackages = "co.hbu.java17springbootconsole")
	@EnableRetry
	public class DemoConfiguration {
	}

}






