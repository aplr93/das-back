package com.webdev.dasback.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webdev.dasback.controller.form.CustomerForm;
import com.webdev.dasback.model.Customer;
import com.webdev.dasback.repository.CustomerRepository;

@RestController
@RequestMapping("/customers")
public class CustomerController {

	@Autowired
	private CustomerRepository customerRepository;
	
	@GetMapping
	public List<Customer> list(){
		return customerRepository.findAll();
	}
	
	@PostMapping
	public void cadastrar(@RequestBody @Valid CustomerForm customerForm) {
		Customer customer = customerForm.convertToCustomer();
		customerRepository.save(customer);
	}
}
