package com.webdev.dasback.controller.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.webdev.dasback.model.Customer;
import com.webdev.dasback.repository.CustomerRepository;

public class CustomerForm {
	
	@NotNull @NotEmpty @Pattern(regexp="\\d{11}")	
	private String cpf;
	@NotNull @NotEmpty @Length(min = 2)
	private String firstName;
	@NotNull @NotEmpty @Length(min = 2)
	private String lastName;

	public Customer convertToCustomer() {
		return new Customer(cpf, firstName, lastName);
	}
	
	public void updateEntity(Customer customer) {
		customer.setCpf(this.cpf);
		customer.setFirstName(this.firstName);
		customer.setLastName(this.lastName);
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
