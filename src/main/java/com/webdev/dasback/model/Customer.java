package com.webdev.dasback.model;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.*;

@Entity
@EnableJpaRepositories
@Table(name="customer")
public class Customer {
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String cpf;
	private String firstName;
	private String lastName;
	
	public Customer() {	}
	
	public Customer(String cpf, String firstName, String lastName) {
		this.cpf = cpf;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
