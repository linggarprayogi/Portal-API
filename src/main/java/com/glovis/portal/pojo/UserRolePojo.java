package com.glovis.portal.pojo;

import java.util.List;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class UserRolePojo {

	private String username;
	
	private String password;
	
	private List<String> roles;
}
