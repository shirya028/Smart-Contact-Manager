package com.smartContactM.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;
import com.smartContactM.dao.ContactRepository;
import com.smartContactM.dao.UserRepositiory;
import com.smartContactM.entities.Contact;
import com.smartContactM.entities.User;
import com.smartContactM.helper.Message;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping(value="/users")
public class UserController {
	
	@Autowired
	private UserRepositiory repo;
	
	@Autowired
	private ContactRepository cRepo;
	
	
	@ModelAttribute
	public void addCommonData(Model m1 , Principal p) {

		String email=p.getName(); //it gives you the current logged in user's username(email) 
		User user=repo.getUserByUserName(email);
		m1.addAttribute("user", user);
	}
	
	@RequestMapping(value="/index")
	public String dashboard(Model m1,Principal p) {
		User u=repo.getUserByUserName(p.getName());
		m1.addAttribute("user",u);
		m1.addAttribute("title", "User Dashboard");
		return "users/user_dashboard";
	}	
	
	@GetMapping("/add-contact")
	public String openContactForm(Model m1) {
		m1.addAttribute("title", "Add Contact");
		m1.addAttribute("contact", new Contact());
		return "users/add_contact";
	}
	
	@PostMapping("/process-contact")
	public String processContactForm(@ModelAttribute Contact contact,Principal  p, @RequestParam("profileImage") MultipartFile file,HttpSession session) {
		 try {
		        String name = p.getName();
		        User user = this.repo.getUserByUserName(name);
			
			
			if(file.isEmpty()) {
				contact.setprofile_image("defualt_image.png");
				System.out.print("File is empty.Default image set to user");
			}
			else {
				File saveFile = new ClassPathResource("static/images").getFile();
	            Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
	            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	   
	            
	            contact.setprofile_image(file.getOriginalFilename());
			}
			
			System.out.println(contact.getprofile_image());
			user.getContacts().add(contact);
	        contact.setUser(user);
	        this.repo.save(user);
	        session.setAttribute("message", new Message("Contact added Successfully" , "alert-success"));
	        System.out.println("Added to db");
		}catch (Exception e) {
			session.setAttribute("message", new Message("error:-"+e.getMessage() ,"alert-danger"));
	        System.out.println("error here" + e.getMessage());
	    }
		
		
		return "redirect:/users/add-contact";
	}
	
	
	@GetMapping("/show-contact/{page}")
	public String showContact(@PathVariable("page")Integer page ,Model m1 , Principal p) {
		String nm=p.getName();
		int id=this.repo.getUserByUserName(nm).getId();
		
		Pageable pageable= PageRequest.of(page, 6);
		Page<Contact> contacts=this.cRepo.findContactsByUser(id,pageable);
		m1.addAttribute("title","All Contacts");
		m1.addAttribute("contacts", contacts);
		m1.addAttribute("currentpage", page);
		m1.addAttribute("totalPages", contacts.getTotalPages());
			
		
		return "users/show_contacts";
	}
	

	@GetMapping("/contacts-details/{cId}")
	public String showContactDetails(@PathVariable("cId") Integer cId ,Model m1,Principal p) {
		
		Optional<Contact> contactOpt= this.cRepo.findById(cId);
		Contact contact=contactOpt.get();
		User u=this.repo.getUserByUserName(p.getName());
		if(contact.getUser().getId() == u.getId())
			m1.addAttribute("contact",contact);
		return "users/contact_detail";
	}
	
	@GetMapping("/delete/{cId}")
	public String deleteContact(@PathVariable("cId") Integer cId,Model m1,Principal p,HttpSession session) {
		
		Optional<Contact> conOpt=this.cRepo.findById(cId);
		Contact contact=conOpt.get();
		
		User u=this.repo.getUserByUserName(p.getName());
		if(contact.getUser().getId() == u.getId()) {
			contact.setUser(null);
			this.cRepo.delete(contact);
			
			session.setAttribute("message", new Message("Contact Deleted Successfully !","alert-success"));
		}
		else {
			session.setAttribute("message", new Message("Invalid Activity !","alert-danger"));
		}
		
		return "redirect:/users/show-contact/0";
	}
	
	@GetMapping("/update/{cId}")
	public String updateContact(@PathVariable("cId")Integer cId ,Model m1) {
		m1.addAttribute("title" , "Update Contact");
		Optional<Contact> conOpt = this.cRepo.findById(cId);
		Contact  contact= conOpt.get();
		m1.addAttribute("contact" , contact);
		return "users/update_contact";
	}
	
	@PostMapping("/process-update") 
	public String processUpdateForm(@ModelAttribute Contact contact,Principal  p, @RequestParam("profileImage") MultipartFile file,HttpSession session) 
	{
		try {
	        String name = p.getName();
	        User user = this.repo.getUserByUserName(name);

			Contact oldContact=this.cRepo.findById(contact.getcId()).get();
		
			if(file.isEmpty()) {
				contact.setprofile_image(oldContact.getprofile_image());
			}
			else {
				if(file.getOriginalFilename()!="defualt_image.png") {
					File deleteFile = new ClassPathResource("static/images").getFile();
					File file1=new File(deleteFile,oldContact.getprofile_image());
					file1.delete();
				}
				File saveFile = new ClassPathResource("static/images").getFile();
	            Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
	            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	   
	            
	            contact.setprofile_image(file.getOriginalFilename());
			}

			for(int i=0;i<user.getContacts().size();i++) {
				if(user.getContacts().get(i).getcId() == contact.getcId()) {
					user.getContacts().remove(i);
					System.out.println("Updated");
					break;
				}
				
			}
			user.getContacts().add(contact);
	        contact.setUser(user);
	        this.repo.save(user);
	        session.setAttribute("message", new Message("Contact updated Successfully" , "alert-success"));
	        System.out.println("Added to db");
		}catch (Exception e) {
			session.setAttribute("message", new Message("error:-"+e.getMessage() ,"alert-danger"));
	        System.out.println("error here" + e.getMessage());
	    }
		return "redirect:/users/contacts-details/"+contact.getcId();
	}
	
	@GetMapping("/user-profile")
	public String userProfile(Model m1,Principal p) {
		User user=repo.getUserByUserName( p.getName());
		m1.addAttribute("user" ,user);
		m1.addAttribute("title","Profile page");
		return "users/user_profile";
	}
	
	@GetMapping("/user-setting")
	public String userSetting() {
		return "users/user_setting";
	}
	
	
}


