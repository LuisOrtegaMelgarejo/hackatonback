package com.oip.helpdesk.controller;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.oip.helpdesk.domain.entities.*;
import com.oip.helpdesk.repository.RoleRepository;
import com.oip.helpdesk.repository.UserTypeRepository;
import com.oip.helpdesk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.oip.helpdesk.domain.exceptions.ResourceNotFoundException;
import com.oip.helpdesk.repository.AreaRepository;
import com.oip.helpdesk.repository.UserRepository;


@CrossOrigin
@RestController
@RequestMapping("/api")

public class UserController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AreaRepository areaRepository;
	@Autowired
	private UserTypeRepository userTypeRepository;

	@Autowired
    PasswordEncoder passwordEncoder;
	@Autowired
	UserService userService;
	@Autowired
	RoleRepository roleRepository;


	@GetMapping("/roles")
	public List<Role> getRoles() {
		return this.roleRepository.findAll();
	}

	@GetMapping("/typeuser")
	public List<UserType> getType() {
		return this.userTypeRepository.findAll();
	}

	//@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/users")
	public List<User> getUsers(@RequestHeader("Authorization") String header , @RequestBody HashMap<String,String> filters) {
		User user = userService.findByToken(header);
		List<User> list = new ArrayList<>();
		if(user.getRoles().iterator().next().getName().equals("admin") || user.getRoles().iterator().next().getName().equals("superadmin")){
			list = userRepository.findAll();
			if(filters.get("role_id")!=null){
				list=list.stream().filter(p -> filters.get("role_id").contains(p.getRoles().iterator().next().getId().toString())).collect(Collectors.toList());
			}
		}
		return list;
	}


	@PostMapping("/users/create")
	public ResponseEntity<?> createNewUser(@RequestHeader("Authorization") String header,@RequestBody HashMap<String,String> body) {

		User userto = userService.findByToken(header);

		if(!userto.getRoles().iterator().next().getName().equals("superadmin")){
			return ResponseEntity.status(403).build();
		}

		User user = new User();
		user.setPassword(passwordEncoder.encode(body.get("password")));
		user.setActive(true);
		user.setEmail(body.get("email"));
		user.setName(body.get("name"));
		user.setLast_name(body.get("last_name"));

		UserType type = userTypeRepository.findById(new Long(body.get("type_id"))).
				orElseThrow(() -> new ResourceNotFoundException("Type","type_id", 1));
		user.setType(type);

		Area area = areaRepository.findById(new Long(body.get("area_id"))).
				orElseThrow(() -> new ResourceNotFoundException("Area","area_id", body.get("area_id")));
		user.setArea(area);

		HashSet<Role> roles = new HashSet<>();
		Role role = roleRepository.findById(new Long(body.get("role_id"))).
				orElseThrow(() -> new ResourceNotFoundException("Role","role_id", body.get("role_id")));
		roles.add(role);
		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok().build();
	}


	@GetMapping("/users/{userId}")
	public User getUserById(@PathVariable(value = "userId") Long userId){

		 return userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","user_id", userId));

	}


	@PutMapping("/users/{userId}/update")
	public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String header,@PathVariable("userId") Long userId, @RequestBody HashMap<String,String> body) {

		User userto = userService.findByToken(header);

		if(!userto.getRoles().iterator().next().getName().equals("superadmin")){
			return ResponseEntity.status(403).build();
		}

		User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","user_id", userId));
		if (!user.getPassword().equals(body.get("password"))){
			user.setPassword(passwordEncoder.encode(body.get("password")));
		}
		user.setEmail(body.get("email"));
		user.setName(body.get("name"));
		user.setLast_name(body.get("last_name"));
		Area area = areaRepository.findById(new Long(body.get("area_id"))).
				orElseThrow(() -> new ResourceNotFoundException("Area","area_id", body.get("area_id")));
		user.setArea(area);
		UserType type = userTypeRepository.findById(new Long(body.get("type_id"))).
				orElseThrow(() -> new ResourceNotFoundException("Type","type_id", 1));
		user.setType(type);

		HashSet<Role> roles = new HashSet<>();
		Role role = roleRepository.findById(new Long(body.get("role_id"))).
				orElseThrow(() -> new ResourceNotFoundException("Role","role_id", body.get("role_id")));
		roles.add(role);
		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok().build();
	}


	@DeleteMapping("/users/{userId}")
	public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String header,@PathVariable(value = "userId") Long userId) {

		User userto = userService.findByToken(header);

		if(!userto.getRoles().iterator().next().getName().equals("superadmin")){
			return ResponseEntity.status(403).build();
		}

		User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","user_id", userId));
		userRepository.delete(user);
		return ResponseEntity.ok().build();

	}

}
