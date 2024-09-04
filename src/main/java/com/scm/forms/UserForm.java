package com.scm.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserForm {
    @NotBlank(message="username is required")
    @Size(min=3,message="Min 3 characters required")
private String name;
@NotBlank(message="Email is required")
@Email(message="Invalid email Address")
private String email;
@NotBlank(message="password is required")
@Size(min=6,max=15,message="Minimum 6 character ")
private String password;
@NotBlank(message="About is required")
private String about;
@NotBlank
 @Size(min=8,max=12,message="Invalid phone number")

private String phoneNumber;

}
