package com.smartContactM.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartContactM.dao.UserRepositiory;
import com.smartContactM.entities.User;
import com.smartContactM.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {

	
	@Autowired
	private UserRepositiory uRepo;
	
	@Autowired
	private BCryptPasswordEncoder passEncoder;
	
	@RequestMapping("/home")
	public String home(Model m1) {
		
		m1.addAttribute("title","Home Page");
		return "home";
	}
	@RequestMapping("/signup")
	public String signUp(Model m1) {
		
		m1.addAttribute("title","register");
		m1.addAttribute("user" ,new User());
		return "signup";
	}
	@RequestMapping("/signin")
	public String signIn(Model m1) {
		m1.addAttribute("title","Login Page");
		return "signin";
	}
	
	
	@PostMapping("/do_register")
	public String doRegister(@Valid  @ModelAttribute("user") User user , BindingResult result, @RequestParam(value="agreement" ,defaultValue="false")Boolean agreement,Model m1,HttpSession session) {
		
		try {
			if(!agreement) {
				throw new Exception("You have to agree the terms and condition");
			}
			if(result.hasErrors()) {
				
				m1.addAttribute("user",user);
				return "signup";
			}
			
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			user.setPassword(passEncoder.encode(user.getPassword()));
			User u=this.uRepo.save(user);
			m1.addAttribute("user",new User());
			session.setAttribute("message", new Message("Successfully Registered" ,"alert-success"));
		}
		catch(Exception e) {
			session.setAttribute("message", new Message("Something went wrong :-"+e.getMessage() , "alert-danger"));
		}
		return "signup";
	}
//	
//	@RequestMapping("/logout")
//    public String logout() {
//        return "redirect:/signin";
//    }
//	
	
	
}
