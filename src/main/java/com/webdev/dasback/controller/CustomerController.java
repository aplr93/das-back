package com.webdev.dasback.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
	public List<Customer> list(){
		return customerRepository.findAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Customer> findById(@PathVariable Long id) {
		Optional<Customer> topicoOptional = customerRepository.findById(id);
		if(topicoOptional.isPresent()) {
			return ResponseEntity.ok(topicoOptional.get()) ;
		}
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public ResponseEntity<Customer> create(@RequestBody @Valid CustomerForm customerForm, UriComponentsBuilder uriBuilder) {
		Customer customer = customerForm.convertToCustomer();
		customerRepository.save(customer);
		
		//Returns the same resource that was created as a JSON response to the front end
		URI uri = uriBuilder.path("/customers/{id}").buildAndExpand(customer.getId()).toUri(); 
		return ResponseEntity.created(uri).body(customer);
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<Customer> update(@PathVariable Long id, @RequestBody @Valid CustomerForm form) {
		Optional<Customer> customerOptional = customerRepository.findById(id);
		if (customerOptional.isPresent()) {
			Customer customer = form.update((long)id, customerRepository);
			return ResponseEntity.ok(customer);
		}
		return ResponseEntity.notFound().build();
	}
	
	@Transactional
	@DeleteMapping("/{id}")
	public ResponseEntity<?> remove(@PathVariable Long id) {
		Optional<Customer> topicoOptional = customerRepository.findById(id);
		if (topicoOptional.isPresent()) {
			customerRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}
}
