package com.learning.dto;

import com.learning.Enum.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Schema(name = "Accounts")
public class AccountsDto {

  @Schema(enumAsRef = true)
  @Enumerated(EnumType.STRING)
  AccountType accountType;

  @Schema(example = "123 NEW YORK")
  @Size(min = 20, max = 200)
  String branchAddress;
}
