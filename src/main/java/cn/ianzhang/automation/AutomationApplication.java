package cn.ianzhang.automation;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Ian
 */
@SpringBootApplication
@SpringBootConfiguration
public class AutomationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutomationApplication.class, args);
	}

}
