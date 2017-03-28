package com.assignment.restapi;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfiguration {

	@Value("${email.protocol}")
	private String protocol;
	@Value("${email.host}")
	private String host;
	@Value("${email.port}")
	private int port;
	@Value("${email.auth}")
	private String auth;
	@Value("${email.starttls}")
	private String starttls;
	@Value("${email.username}")
	private String username;
	@Value("${email.password}")
	private String password;

	@Bean
	public JavaMailSender javaMailService() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

		javaMailSender.setHost(host);
		javaMailSender.setPort(port);
		javaMailSender.setProtocol(protocol);
		javaMailSender.setUsername(username);
		javaMailSender.setPassword(password);

		javaMailSender.setJavaMailProperties(getMailProperties());

		return javaMailSender;
	}

	private Properties getMailProperties() {
		Properties emailProperties = new Properties();
		emailProperties.setProperty("mail.smtp.auth", auth);
		emailProperties.setProperty("mail.smtp.starttls.enable", starttls);
		emailProperties.setProperty("mail.debug", "true");

		return emailProperties;
	}

}
