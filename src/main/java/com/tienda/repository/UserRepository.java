package com.tienda.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tienda.model.User;
@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

	@Query("SELECT user FROM User user WHERE user.name = :username")
	Optional<User> findByUsername(@Param("username")String username);
	
	@Query("SELECT user FROM User user WHERE user.email = :email")
	Optional<User> findByEmail(@Param("email")String email);
}
