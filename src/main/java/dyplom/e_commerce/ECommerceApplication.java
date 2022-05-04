package dyplom.e_commerce;

import dyplom.e_commerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ECommerceApplication {


	public static void main(String[] args) {
		SpringApplication.run(ECommerceApplication.class, args);
	}
}
