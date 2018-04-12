package rest;

import java.util.Optional;
import java.util.Properties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TwitterDemoApp {
	public static final Properties myProps = new Properties();
	public static final Optional<String> host = Optional.ofNullable(System.getenv("HOSTNAME"));
	public static final Optional<String> port = Optional.ofNullable(System.getenv("PORT"));

	public static void main(String[] args) {
		myProps.setProperty("server.address", (String) host.orElse("localhost"));
		myProps.setProperty("server.port", (String) port.orElse("8080"));
		SpringApplication app = new SpringApplication(new Object[] { TwitterDemoApp.class });
		app.setDefaultProperties(myProps);
		app.run(args);
	}

}