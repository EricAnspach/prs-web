package com.prs.web;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.prs.business.user.User;
import com.prs.business.user.UserRepository;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepo;
	
	@GetMapping("/")
	public JsonResponse getAll() {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(userRepo.findAll());
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;		
	}
	
	@GetMapping("/{id}")
	public JsonResponse get(@PathVariable int id) {
		JsonResponse jr = null;		
		try {
			Optional<User> u = userRepo.findById(id);
			if (u.isPresent()) {
				jr = JsonResponse.getInstance(u);
			} else {
				jr = JsonResponse.getInstance(new Exception("No user found for id = " + id));
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}		
		return jr;			
	}
	
	@PostMapping("/")
	public JsonResponse addUser(@RequestBody User u) {
		JsonResponse jr = null;
		jr = JsonResponse.getInstance(saveUser(u));	
		return jr;
	}
	
	@PutMapping("/{id}")
	public JsonResponse updateUser(@RequestBody User u, @PathVariable int id) {
		return saveUser(u);
	}

	private JsonResponse saveUser(User user) {
		JsonResponse jr = null;
		try {
			userRepo.save(user);
			jr = JsonResponse.getInstance(user);
		} catch (DataIntegrityViolationException e) {
			jr = JsonResponse.getInstance(new Exception(e.getMessage()));
		}
		return jr;
	}
	
	@DeleteMapping("/{id}")
	public @ResponseBody JsonResponse deleteUser(@PathVariable int id) {
		JsonResponse jr = null;
		Optional<User> u = userRepo.findById(id);
		try {
			if (u.isPresent()) {
				userRepo.deleteById(id);
				jr = JsonResponse.getInstance(u);
			} else {
				jr = JsonResponse.getInstance(new Exception("User delete unsuccessful, user " + id + " does not exist."));
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
	
	@GetMapping("/getByUsername")
	public JsonResponse getUserByUsername(@RequestBody User u) {
		return JsonResponse.getInstance(userRepo.findByUserName(u.getUserName()));
	}
}
