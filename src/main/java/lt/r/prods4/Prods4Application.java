package lt.r.prods4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Prods4Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Prods4Application.class, args);
		System.out.println("Hey");
		System.out.println("name "+context.getApplicationName());
			}
}
