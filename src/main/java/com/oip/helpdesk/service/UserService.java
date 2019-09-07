package com.oip.helpdesk.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.oip.helpdesk.domain.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oip.helpdesk.domain.entities.User;
import com.oip.helpdesk.repository.UserRepository;

import static com.oip.helpdesk.security.Constants.SECRET;
import static com.oip.helpdesk.security.Constants.TOKEN_PREFIX;

@Service
public class UserService {
	@Autowired
	UserRepository userRepository;
	
	public List<User> getAll(){
		return userRepository.findAll();
	}

	public Optional<User> findById(long id) {
		return userRepository.findById(id);
		
	}

	public  User findByToken(String token){
		String email = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
				.build()
				.verify(token.replace(TOKEN_PREFIX, ""))
				.getSubject();
		User user = userRepository.findByEmail(email).orElseThrow(()
				-> new ResourceNotFoundException("email","email", email));

		return  user;
	}
		
}
