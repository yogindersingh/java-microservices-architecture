package com.learning.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
@Schema(name = "Customer")
public class CustomerDto {

  @Size(min = 5, max = 20)
  @Schema(example = "Bryan lara")
  String name;

  @Email(message = "email is not correct")
  @Schema(example = "example@gmail.com")
  String email;

  @Size(min = 10,max = 10,message = "Mobile number should be length 10")
  @Schema(example = "1234567890")
  String mobileNumber;

  AccountsDto accountsDto;
}
