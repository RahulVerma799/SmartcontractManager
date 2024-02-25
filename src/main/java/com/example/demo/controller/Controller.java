package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.Dao.UserRepository;
import com.example.demo.entity.User;

public class Controller {
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/test")
	@ResponseBody
	public String test() {
		
		User user=new User();
		
		user.setName("rahl");
		user.setEmail("toi@tc.in");
		
		userRepository.save(user);
		return "working";
	}
	

}
