package com.webdev.dasback.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	public List<Customer> getCustomer(@RequestParam(required=false, name="cpf") String cpf){
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
	public ResponseEntity<Object> createCustomer(@RequestBody @Valid CustomerForm customerForm, UriComponentsBuilder uriBuilder) {
		Customer customer = customerForm.convertToCustomer();

		if(cpfAlreadyRegistered(customer.getCpf())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Já existe um cliente com o CPF " + customerForm.getCpf() + "!");
		}
		
		return commitCustomerCreation(customer, uriBuilder);		
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<Object> updateCustomer(@PathVariable Long id, @RequestBody @Valid CustomerForm form) {
		Optional<Customer> customerOptional = customerRepository.findById(id);	
		return validateAndProcessCustomerUpdate(customerOptional, form);	
	}

	@Transactional
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> removeCustomer(@PathVariable Long id) {
		Optional<Customer> customerOptional = customerRepository.findById(id);
		return validateAndProcessCustomerRemoval(customerOptional);
	}

	private ResponseEntity<Object> validateAndProcessCustomerRemoval(Optional<Customer> customerOptional) {
		ResponseEntity<Object> errorResponseEntity = buildCustomerRemovalErrorResponse(customerOptional);
		if (errorResponseEntity != null) {
			return errorResponseEntity;
		}

		return commitCustomerRemoval(customerOptional.get());
	}

	private ResponseEntity<Object> buildCustomerRemovalErrorResponse(Optional<Customer> customerOptional) {
		if(customerOptional.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return null;
	}

	private ResponseEntity<Object> commitCustomerRemoval(Customer customer) {
		customerRepository.deleteById(customer.getId());
		return ResponseEntity.ok(customer);
	}
	
	private ResponseEntity<Object> commitCustomerUpdate(Customer customer, CustomerForm form) {
		form.updateEntity(customer);
		return ResponseEntity.ok(customer);
	}

	private ResponseEntity<Object> buildCustomerUpdateErrorResponse(Optional<Customer> customerOptional,
			@Valid CustomerForm form) {
		if(customerOptional.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		if(previousCpfWasModified(customerOptional, form) && cpfAlreadyRegistered(form.getCpf())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Já existe um cliente com o CPF " + form.getCpf() + "!");
		}
		return null;
	}

	private boolean previousCpfWasModified(Optional<Customer> customerOptional, CustomerForm form) {
		boolean returnValue = true;
		if(customerOptional.isPresent()) {
			returnValue = ! customerOptional.get().getCpf().equals(form.getCpf());  
		}
		return returnValue;
	}
	
	private boolean cpfAlreadyRegistered(String cpf) {
		List<Customer> customersWithSameCpf = customerRepository.findByCpf(cpf);
		return customersWithSameCpf.size() > 0;
	}

	private ResponseEntity<Object> commitCustomerCreation(Customer customer, UriComponentsBuilder uriBuilder) {
		customerRepository.save(customer);
		
		//Returns the same resource that was created as a JSON response to the front end
		URI uri = uriBuilder.path("/customers/{id}").buildAndExpand(customer.getId()).toUri(); 
		return ResponseEntity.created(uri).body(customer);
	}
	
	private ResponseEntity<Object> validateAndProcessCustomerUpdate(Optional<Customer> customerOptional,
			CustomerForm form) {
		ResponseEntity<Object> errorResponse = buildCustomerUpdateErrorResponse(customerOptional, form);
		if(errorResponse != null) {
			return errorResponse;
		}
		
		return commitCustomerUpdate(customerOptional.get(), form);
	}
}
