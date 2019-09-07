package com.oip.helpdesk.repository;


import com.oip.helpdesk.domain.entities.User;
import com.oip.helpdesk.domain.entities.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTypeRepository extends JpaRepository<UserType, Long> {

}