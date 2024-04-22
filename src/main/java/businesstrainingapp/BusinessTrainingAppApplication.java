package businesstrainingapp;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "BusinessTrainingApp"))
public class BusinessTrainingAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BusinessTrainingAppApplication.class, args);
	}

}
