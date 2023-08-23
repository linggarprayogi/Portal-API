package com.glovis.portal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.glovis.portal.dto.UserRoleDTO;
import com.glovis.portal.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	
	Optional<User> findByUsername(String username);
}
