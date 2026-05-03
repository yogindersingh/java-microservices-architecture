package com.learning.controller;

import com.learning.dto.LoanDto;
import com.learning.dto.ErrorResponseDto;
import com.learning.dto.ResponseDto;
import com.learning.service.ILoansService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@Tag(name = "Loans API",description = "Loans API")
public class LoansController {

  @Autowired
  ILoansService iLoansService;

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
  public ResponseEntity<ResponseDto> fetchLoan(@RequestParam(name = "mobileNumber") @Size(min=10,max=10) @Schema(example = "1234567890") String mobileNumber) {
    LoanDto loanDto = iLoansService.getLoan(mobileNumber);
    return ResponseEntity.ok(new ResponseDto(loanDto, HttpStatus.OK));
  }

}
