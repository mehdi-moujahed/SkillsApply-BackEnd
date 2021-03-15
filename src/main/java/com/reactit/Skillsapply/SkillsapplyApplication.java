package com.reactit.Skillsapply;

import com.reactit.Skillsapply.model.User;
import com.reactit.Skillsapply.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableSwagger2
public class SkillsapplyApplication {

//	@Autowired
//	private UserRepository userRepository;
//
//	@Autowired
//	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {

		SpringApplication.run(SkillsapplyApplication.class, args);

//		LocalDate now = LocalDate.now();
//		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd LLLL yyyy");
//		System.out.println(now.format(dtf));

	}

//	@Override
//	public void run(String... args) throws Exception {
//		if (this.userRepository.findByEmail("moujahedmehdi@gmail.Com") == null) {
//
////			User user = new User("Mehdi", "Moujahed", "moujahedmehdi@gmail.Com", passwordEncoder.encode("mehdi123"), Arrays.asList("ADMIN"));
//			User user = new User("Mehdi","Moujahed","moujahedmehdi@gmail.com", 55287186, 1997-01-26,"ss","sousse", passwordEncoder.encode("mehdi123"),Arrays.asList("ADMIN"));
//			this.userRepository.save(user);
//		}
//	}
}
