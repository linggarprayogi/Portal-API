package com.glovis.portal.dto;

import java.util.List;

public interface UserRoleDTO {

	String getEmail();
	
	String getUsername();
	
	String getPassword();
	
	List<String> getRoles();
	
}
