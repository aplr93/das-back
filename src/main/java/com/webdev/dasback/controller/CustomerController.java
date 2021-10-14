package com.webdev.dasback.controller;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<Customer> atualizar(@PathVariable Long id, @RequestBody @Valid CustomerForm form) {
		Optional<Customer> customerOptional = customerRepository.findById(id);
		if (customerOptional.isPresent()) {
			Customer customer = form.update((long)id, customerRepository);
			return ResponseEntity.ok(customer);
		}
		return ResponseEntity.notFound().build();
	}
}
