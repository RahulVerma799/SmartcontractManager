package com.example.demo.HomeCont;

import java.security.Principal;

import java.util.Optional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.demo.Dao.ContactRepository;
import com.example.demo.Dao.UserRepository;
import com.example.demo.entity.Contact;
import com.example.demo.entity.User;
import com.example.demo.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	
	@ModelAttribute
	public void addcommonData(Model model,Principal principal) {
		String userName=principal.getName();
		System.out.println("USERNAME"+userName);
		
		User user=userRepository.getUserbyUserName(userName);
		System.out.println("USER"+user);
		model.addAttribute("user",user);
		
	}
	
	@GetMapping("/index")
	public String dashboard(Model model,Principal principal){
//		String userName=principal.getName();
//		System.out.println("USERNAME"+userName);
//		User user=userRepository.getUserbyUserName(userName);
//		System.out.println("USER"+user);
//		model.addAttribute("user",user);
		
		return "Normal/user_dashboard";
		
	}
	
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model) {
		model.addAttribute("title","Add-contact");
		model.addAttribute("contact",new Contact());
		return "Normal/add_contact";
	}
	
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact, Principal principal, HttpSession session) {
		
			try {
		
			String name= principal.getName();
			User user=this.userRepository.getUserbyUserName(name);
			contact.setUser(user);
			user.getContacts().add(contact);
			this.userRepository.save(user);
		
			System.out.println("DATA"+contact);
			System.out.println("added to the database");
			session.setAttribute("message",new Message("Your contact is needed","success"));
			
			//HttpSession session1=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
			//session1.removeAttribute("message");
			
			
			}catch (Exception e) {
				System.out.println("ERROR"+e.getMessage());
				e.printStackTrace();
				session.setAttribute("message",new Message("Somthing went Wrong","Danger"));
			}
			return "Normal/add_contact";
	}
	
	@GetMapping("/show-contact/{page}")
	public String showContact(@PathVariable("page")Integer page,Model model,Principal principal) {
		model.addAttribute("title","Show user contacts");
		
		String userName=principal.getName();
		User user=this.userRepository.getUserbyUserName(userName);
//		List<Contact> contacts=user.getContacts();
		
		Pageable pageable= PageRequest.of(page, 5);
//		
		Page<Contact> contacts= this.contactRepository.findContactsByUser(user.getId(),pageable);
		
		model.addAttribute("contacts",contacts);
		model.addAttribute("currentPage",page);
		model.addAttribute("totalPages",contacts.getTotalPages());
		
		return "Normal/show_contact";
	}
	
	@GetMapping("/{cid}/contact")
	public String showContactDetail(@PathVariable("cid") Integer cid,Model model,Principal principal) {
		
		try {
		Optional<Contact> contactOptional=this.contactRepository.findById(cid);
			Contact contact=contactOptional.get();
			
			String userName=principal.getName();
			User user=this.userRepository.getUserbyUserName(userName);
			if(user.getId()==contact.getUser().getId());
			{
			model.addAttribute("contact",contact);
			model.addAttribute("title",contact.getName());
			}
		
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "Normal/contact_detail";
	}
	
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cid,Model model,HttpSession session) {
		
		Contact contact=this.contactRepository.findById(cid).get();
		
		contact.setUser(null);
		this.contactRepository.delete(contact);
		
		session.setAttribute("message",new Message( "contact deleted","success"));
		
		return "redirect:/user/show-contact/0";
	}
	
	@PostMapping("/update-contact/{cid}")
	public String updateForm(@PathVariable("cid") Integer cid,Model model) {
		model.addAttribute("title","update contact");
		
		Contact contact=this.contactRepository.findById(cid).get();
		model.addAttribute("contact",contact);
		
		return "Normal/update_form";
	}
	
	@PostMapping("/process-update")
	public String updateForm(@ModelAttribute Contact contact,Model model,Principal principal,HttpSession session) {
		
		try {
		 User user=	this.userRepository.getUserbyUserName(principal.getName());
		
		 contact.setUser(user);
		 this.contactRepository.save(contact);
		 
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	@GetMapping("/Profile")
	public String yourProfile(Model model,Principal principal, HttpSession session ) {
		model.addAttribute("title","Profile page");
		return "Normal/profile";
		}
	

}
