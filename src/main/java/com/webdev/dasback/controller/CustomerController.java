package com.webdev.dasback.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import com.webdev.dasback.controller.form.CustomerForm;
import com.webdev.dasback.model.Customer;
import com.webdev.dasback.repository.CustomerRepository;

@RestController
@RequestMapping("/customers")
public class CustomerController {

	@Autowired
	private CustomerRepository customerRepository;
	
	@GetMapping
	public List<Customer> getCustomer(String cpf){
		// returns all customers or filter by cpf
		// example by cpf - http://localhost:8111/customers?cpf=66666666666
		// example all customers - http://localhost:8111/customers
		if (cpf == null) {
			return customerRepository.findAll();
		}
		else {
			return customerRepository.findByCpf(cpf);
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
		Optional<Customer> customerOptional = customerRepository.findById(id);
		if(customerOptional.isPresent()) {
			return ResponseEntity.ok(customerOptional.get()) ;
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<Customer> createCustomer(@RequestBody @Valid CustomerForm customerForm, UriComponentsBuilder uriBuilder) {
		Customer customer = customerForm.convertToCustomer();

		List<Customer> checkCpfRepeat = customerRepository.findByCpf(customer.getCpf());

		if(checkCpfRepeat.size() > 0) {
			return ResponseEntity.notFound().build();
		}

		customerRepository.save(customer);
		
		//Returns the same resource that was created as a JSON response to the front end
		URI uri = uriBuilder.path("/customers/{id}").buildAndExpand(customer.getId()).toUri(); 
		return ResponseEntity.created(uri).body(customer);
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody @Valid CustomerForm form) {
		Optional<Customer> customerOptional = customerRepository.findById(id);
		if (customerOptional.isPresent()) {
			Customer customer = form.update((long)id, customerRepository);
			return ResponseEntity.ok(customer);
		}
		return ResponseEntity.notFound().build();
	}
	
	@Transactional
	@DeleteMapping("/{id}")
	public ResponseEntity<?> removeCustomer(@PathVariable Long id) {
		Optional<Customer> customerOptional = customerRepository.findById(id);
		if (customerOptional.isPresent()) {
			customerRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}
}
