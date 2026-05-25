package com.learning.controller;

import com.learning.dto.AccountsContactInfoDto;
import com.learning.dto.CustomerDto;
import com.learning.dto.ErrorResponseDto;
import com.learning.dto.ResponseDto;
import com.learning.service.AccountsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@Tag(name = "Accounts", description = "Accounts Endpoints")
public class AccountsController {

  @Autowired
  private AccountsService accountsService;

  @Value("${build.info}")
  private String buildInfo;

  @Autowired
  private Environment environment;

  @Autowired
  private AccountsContactInfoDto accountsContactInfoDto;

  @ApiResponses({
      @ApiResponse(responseCode = "200",description = "Account created successfully"),
      @ApiResponse(responseCode = "400",description = "validation Error occured"),
      @ApiResponse(
          responseCode = "500",
          description = "HTTP Status Internal Server Error",
          content = @Content(
              schema = @Schema(implementation = ErrorResponseDto.class)
          )
      )
  }
  )
  @Operation(description = "Create new account")
  @PostMapping("/create")
  public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody
                                                   CustomerDto customerDto) {
    return new ResponseEntity<>(accountsService.createAccount(customerDto), HttpStatus.CREATED);
  }

  @Operation(description = "Fetch existing account using mobile number")
  @GetMapping("/fetch/{mobileNumber}")
  public ResponseEntity<ResponseDto> getAccounts(@PathVariable(name = "mobileNumber") String mobileNumber) {
    return new ResponseEntity<>(accountsService.getAccountByMobileNumber(mobileNumber), HttpStatus.OK);
  }

  @Operation(description = "Fetch build information")
  @GetMapping("/build-info")
  public ResponseEntity<ResponseDto> getBuildInfo() {
    return new ResponseEntity<>(new ResponseDto(buildInfo,HttpStatus.OK), HttpStatus.OK);
  }

  @Operation(description = "Fetch java version")
  @GetMapping("/java-version")
  public ResponseEntity<ResponseDto> getJavaVersion() {
    return new ResponseEntity<>(new ResponseDto(environment.getProperty("java.version"),HttpStatus.OK), HttpStatus.OK);
  }

  @Operation(description = "Fetch contact information")
  @GetMapping("/contact-info")
  public ResponseEntity<ResponseDto> getContactInfo() {
    return new ResponseEntity<>(new ResponseDto(accountsContactInfoDto,HttpStatus.OK), HttpStatus.OK);
  }

  @Operation(description = "fetch customer complete details")
  @GetMapping("/fetchCustomerDetails")
  public ResponseEntity<ResponseDto> fetchCustomerDetails(@RequestParam(name = "mobileNumber") String mobileNumber,
                                                          @RequestHeader(name = "X-Correlation-ID")  String correlationId) {
    return new ResponseEntity<>(accountsService.getCustomerDetails(correlationId,mobileNumber),HttpStatus.OK);
  }
}
