package com.smartContactM.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smartContactM.dao.ContactRepository;
import com.smartContactM.dao.UserRepositiory;
import com.smartContactM.entities.Contact;
import com.smartContactM.entities.User;

@RestController
public class SearchController {

	@Autowired
	private UserRepositiory uRepo;
	
	@Autowired
	private ContactRepository cRepo;
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query, Principal p) {
		User u=this.uRepo.getUserByUserName(p.getName());
		List<Contact> contacts=cRepo.findByNameContainingAndUser(query, u);
		return ResponseEntity.ok(contacts);

		
	}
}
