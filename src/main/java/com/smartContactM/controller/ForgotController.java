package com.smartContactM.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartContactM.dao.UserRepositiory;
import com.smartContactM.entities.User;
import com.smartContactM.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class ForgotController {
	
	@Autowired
	private UserRepositiory uRepo;
	
	@GetMapping("/forgot")
	public String forgot_pass(Model m1) {
		m1.addAttribute("name", "generate OTP");
		return "forgot_password_form";
	}
	
	@PostMapping("/process-otp") 
	public String process_otp(@RequestParam("otp") String action,@RequestParam("forgot_email") String email,HttpSession session,Model m1) {
		
		if(action.equals("generateOTP")) 
		{
			User u=uRepo.getUserByUserName(email);
			if(u ==  null) {
				session.setAttribute("message" , new Message("No Registerd User Found","alert-danger"));
				m1.addAttribute("name", "generate OTP");
			}
			else {
				Random rand=new Random(1000);
				int otp=rand.nextInt(99999);
				//send mail funn

				m1.addAttribute("name", "Validate OTP");
				session.setAttribute("message" , new Message("Mail sent to your registerd mail !","alert-success"));
			}
			
			return "forgot_password_form";
		}
		else  {
			
			return "forgot_password_form";
		}
	}
	
	
}
