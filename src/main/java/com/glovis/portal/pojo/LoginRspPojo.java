package com.glovis.portal.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class LoginRspPojo {

	private UserRolePojo user;
	private String accessToken;
	private String refreshToken;

	public LoginRspPojo(UserRolePojo user, String accessToken, String refreshToken) {
		this.user       = user;
		this.accessToken  = accessToken;
		this.refreshToken = refreshToken;
	}
}