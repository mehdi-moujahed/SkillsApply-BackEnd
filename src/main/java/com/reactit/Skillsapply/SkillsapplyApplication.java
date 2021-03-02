package com.reactit.Skillsapply;

import com.reactit.Skillsapply.model.User;
import com.reactit.Skillsapply.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@SpringBootApplication
public class SkillsapplyApplication implements CommandLineRunner {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SkillsapplyApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (this.userRepository.findByUsername("rokin") == null) {

			User user = new User("Rokin Maharjan", "rokin", passwordEncoder.encode("rokin123"), Arrays.asList("ADMIN"));

			this.userRepository.save(user);
		}
	}
}
