package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.entities.User;
import com.example.demo.services.UserService;

@RestController
@RequestMapping("/users")
@CrossOrigin(originPatterns = "*") //a kind of cors configuration
public class UserController {
	
	@Autowired
	private UserService service;
	
	@GetMapping
	private List<User> list(){
		return service.findAll();
	}
	
	@GetMapping("/{id}") //path variable
	public ResponseEntity<?> show(@PathVariable Long id) {
		Optional< User> userOptional = service.findById(id);
		
		if (userOptional.isPresent()) {
			return ResponseEntity.ok(userOptional.orElseThrow());
		}
		return ResponseEntity.notFound().build();
	}	
	
	
	@PostMapping
	public ResponseEntity<?>  create(@RequestBody User user ) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(user));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@RequestBody User user, @PathVariable Long id){
		 
		//serach id or user
		Optional<User> o = service.update(user, id);
		
		if (o.isPresent()) {
			
			return ResponseEntity.status(HttpStatus.CREATED).body(o.orElseThrow());
			
		}
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> remove(@PathVariable Long id){
		
		Optional<User> o = service.findById(id);
		if (o.isPresent()) {
			service.remove(id);
			
			return ResponseEntity.noContent().build();	//204
		} 
		return ResponseEntity.notFound().build();
	}
	

	
	
	
	
	
	
}
