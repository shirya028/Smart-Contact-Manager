package com.smartContactM.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smartContactM.dao.UserRepositiory;
import com.smartContactM.entities.User;

public class UserDetailsConfigImpl implements UserDetailsService{

	
	@Autowired
	private UserRepositiory userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		
		User user=userRepo.getUserByUserName(username);
		
		if(user==null)
			throw new UsernameNotFoundException("Counld not find user");
		
		CustomUserClass c=new CustomUserClass(user);
		return c;
	}

}
