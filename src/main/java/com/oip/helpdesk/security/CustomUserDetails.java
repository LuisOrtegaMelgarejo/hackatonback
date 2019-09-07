package com.oip.helpdesk.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.oip.helpdesk.domain.entities.User;


public class CustomUserDetails extends User implements UserDetails{

	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 331733542920342529L;

	public CustomUserDetails(final User user) {
	        super(user);
	    }

	 @Override
	 public Collection<? extends GrantedAuthority> getAuthorities() {

	        return getRoles()
	                .stream()
	                .map(role -> new SimpleGrantedAuthority(role.getName()))
	                .collect(Collectors.toList());
	  }



	@Override
	public String getEmail() {
		return super.getEmail();			
	}
	
	@Override
	public String getPassword() {
		return super.getPassword();			
	}
	
	@Override
	public String getUsername() {		
		return super.getName();
	}
	
	
	@Override
	public boolean isAccountNonExpired() {		
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {		
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {		
		return true;
	}

	@Override
	public boolean isEnabled() {		
		return true;
	}

	

}
