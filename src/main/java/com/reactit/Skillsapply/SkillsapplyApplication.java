package com.reactit.Skillsapply;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableSwagger2
public class SkillsapplyApplication {


	public static void main(String[] args) throws ScriptException {
		SpringApplication.run(SkillsapplyApplication.class, args);
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("javascipt");

		Object result = engine.eval("function add(a, b) {\n" +
				"return a+b\n" +
				"}\n" +
				"console.log(add(4, 6))");
		System.out.println("Result returned by Javascript is: " + result);
	}


}
