package com.example.demo.HomeCont;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Dao.UserRepository;
import com.example.demo.entity.User;
import com.example.demo.helper.Message;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/home")
	public String home(Model model) {
		model.addAttribute("title","home smart manger");	
		return "home";
	}
	
	@GetMapping("/signin")
	public String login(Model model) {
		model.addAttribute("login","Login Page");
		return "login";
	}
	
	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("title","about smart manger");	
		return "about";
	}
	
	SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

	@GetMapping("/logout")
	public String performLogout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
	    // .. perform logout
	    this.logoutHandler.logout(request, response, authentication);
	    return "redirect:/home";
	}
//	@GetMapping("/logout")
//	public String logout() {
//		return "redirect:/home";
//	}
	
	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title","signup- smart manger");	
		model.addAttribute("user",new User());
		return "signup";
	}
	
	@PostMapping(value="/do_register")
	public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult result1,@RequestParam(value="agreement",defaultValue="false")boolean agreement,Model model,HttpSession session)
	{
		try {
			if(!agreement) {
				System.out.println("you have not accepted term and condition");
				throw new Exception("you have not agreed the term and condtion");
			}
			if(result1.hasErrors()) {
				System.out.println("ERROR"+result1.toString());
				model.addAttribute("user",user);
				return "signup";
			}
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			
			System.out.println("agreement"+agreement);
			System.out.println("USER"+ user);
			User result =this.userRepository.save(user);
			
			model.addAttribute("user",new User());
			session.setAttribute("message",new Message("Successfully Register","alert-error"));
			
			
			return "signup";
			
			
		}catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message",new Message("somehting went wrong"+e.getMessage(),"alert-error"));
			
		
		return "signup";
	}
	

}
}