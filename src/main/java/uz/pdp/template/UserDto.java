package uz.pdp.template;


import javax.validation.constraints.NotNull;

import lombok.Data;


@Data
public class UserDto {

	@NotNull(message = "UserName is mandatory")
	private String userFullName;
	
	@NotNull(message = "password is mandatory")
	private String password;
}
