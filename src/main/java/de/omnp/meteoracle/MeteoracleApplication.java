package de.omnp.meteoracle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// import de.omnp.meteoracle.infrastructure.spi.SignerImplementation;

@SpringBootApplication
public class MeteoracleApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeteoracleApplication.class, args);
		// SignerImplementation signer = new SignerImplementation();
		// signer.sign();
	}
}
