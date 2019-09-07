package com.oip.helpdesk.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.oip.helpdesk.domain.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query(value = "SELECT * FROM users WHERE email = ?1",nativeQuery = true)
	Optional<User> findByEmail(String email);

	Optional<User> findByName(String username);

	@Query(value = "SELECT us.* FROM users us \n" +
			"JOIN user_role ru ON ru.user_id = us.id\n" +
			"JOIN role ro ON ro.id = ru.role_id\n" +
			"WHERE ro.name != 'user'",nativeQuery = true)
	List<User> findAdmins();
}