package com.learning.controller;

import com.learning.dto.LoanDto;
import com.learning.dto.ErrorResponseDto;
import com.learning.dto.LoansContactInfoDto;
import com.learning.dto.ResponseDto;
import com.learning.service.ILoansService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@Tag(name = "Loans API",description = "Loans API")
@Slf4j
public class LoansController {

  @Autowired
  ILoansService iLoansService;

  @Value("${build.info}")
  private String buildInfo;

  @Autowired
  private Environment environment;

  @Autowired
  private LoansContactInfoDto loansContactInfoDto;

  @Operation(description = "Create Loan")
  @PostMapping("/create/{mobileNumber}")
  public ResponseEntity<ResponseDto> createLoan(@PathVariable(name = "mobileNumber")
      @Size(min=10,max=10) @Schema(example = "1234567890")
                         String mobileNumber) {
    iLoansService.createLoan(mobileNumber);
    return ResponseEntity.ok(new ResponseDto("Loan created successfully.", HttpStatus.CREATED));
  }

  @Operation(description = "Retrieves loan for mobile number")
  @ApiResponses({
      @ApiResponse(responseCode = "200",description = "content fetched successfully"),
      @ApiResponse(responseCode = "400",content =  @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
  })
  @GetMapping("/fetch")
  public ResponseEntity<ResponseDto> fetchLoan(@RequestHeader(name = "X-Correlation-ID") String correlationId,
                                               @RequestParam(name = "mobileNumber") @Size(min=10,max=10) @Schema(example = "1234567890") String mobileNumber) {
    log.info("X-Correlation-ID found : {}", correlationId);
    LoanDto loanDto = iLoansService.getLoan(mobileNumber);
    return ResponseEntity.ok(new ResponseDto(loanDto, HttpStatus.OK));
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
    return new ResponseEntity<>(new ResponseDto(loansContactInfoDto,HttpStatus.OK), HttpStatus.OK);
  }
}
