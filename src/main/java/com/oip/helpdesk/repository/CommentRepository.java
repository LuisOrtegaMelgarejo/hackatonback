package com.oip.helpdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oip.helpdesk.domain.entities.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>  {

	
	
}
