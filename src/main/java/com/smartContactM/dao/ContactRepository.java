package com.smartContactM.dao;


import org.springframework.data.domain.Pageable;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartContactM.entities.Contact;
import com.smartContactM.entities.User;

public interface ContactRepository extends JpaRepository<Contact,Integer>{

	@Query("from Contact as c where c.user.id=:userId")
	public Page<Contact> findContactsByUser(@Param("userId") int userId , Pageable pePageable);
	
	
	//predefined method....as tought in yt that function name which have important keywords like (by ,Containing) will fetch it
	public List<Contact> findByNameContainingAndUser(String keyword,User user);
}
