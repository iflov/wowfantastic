package idu.cs.controller;

import java.util.List;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import idu.cs.domain.User;
import idu.cs.exception.ResourceNotFoundException;
import idu.cs.repository.UserRepository;

@Controller
public class HomeController {
	@Autowired UserRepository userRepo; // Dependency Injection
	
	
	@GetMapping("/")
	public String home(Model model) {
		//model.addAttribute("test", "인덕 컴소");
		//model.addAttribute("htj", "정횬타이");
		return "index";
	}
	@GetMapping("/users")//읽기 main.js에서 사용하는것
	public String getAllUser(Model model) {
		model.addAttribute("users", userRepo.findAll());
		return "userlist";
	}
	@GetMapping("/user-reg-form")
	public String getRegForm(Model model) {
		
		return "form";
	}
	@PostMapping("/users")//쓰기
	public String createUser(@Valid @RequestBody User user, Model model) {
		
		userRepo.save(user);
		model.addAttribute("users", userRepo.findAll());
		return "redirect:/users";
	}
	
	@GetMapping("/users/{id}")
	public String getUserById(@PathVariable(value = "id") Long userId, Model model)
			throws ResourceNotFoundException {
		
		//UserRepository
		User user = userRepo.findById(userId).get();//orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
		
		model.addAttribute("user", user);
		return "user";
		//return ResponseEntity.ok().body(user);
	}
	@GetMapping("/users/fn")
	public String getUserByName(@Param(value="name") String name, Model model)
			throws ResourceNotFoundException {
		
		//UserRepository
		List<User> users = userRepo.findByName(name);//orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
		
		model.addAttribute("users", users);
		return "userlist";
		//return ResponseEntity.ok().body(user);
	}
	@PutMapping("/users/{id}")//업데이트 @patchmapping 수정한 필드만 고쳐주는것
	public String updateUser(@PathVariable(value = "id") Long userId,@Valid User userDetails, Model model) {
		User user = userRepo.findById(userId).get(); //user는 db로부터 읽어온 객체
		user.setName(userDetails.getName());// userDetails는 전송한 객체
		user.setCompany(userDetails.getCompany());
		userRepo.save(user);
		return "users";
	}	
	@DeleteMapping("/users/{id}")//삭제
	public String deleteUser(@PathVariable(value = "id") Long userId, Model model) {
		User user = userRepo.findById(userId).get();
		userRepo.delete(user);
		model.addAttribute("name", user.getName());
		return "user-deleted";
	}
}
