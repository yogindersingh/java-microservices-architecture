package com.learning.controller;

import com.learning.dto.CardsContactInfoDto;
import com.learning.dto.CardsDto;
import com.learning.dto.ErrorResponseDto;
import com.learning.dto.ResponseDto;
import com.learning.service.ICardsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
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
@Tag(name = "Cards API",description = "cards API")
public class CardsController {

  @Autowired
  ICardsService cardsService;

  @Value("${build.info}")
  private String buildInfo;

  @Autowired
  private Environment environment;

  @Autowired
  private CardsContactInfoDto cardsContactInfoDto;

  @Operation(description = "Create Card")
  @PostMapping("/create/{mobileNumber}")
  public ResponseEntity<ResponseDto> createCard(@PathVariable(name = "mobileNumber")
      @Size(min=10,max=10) @Schema(example = "1234567890")
                         String mobileNumber) {
    cardsService.createCard(mobileNumber);
    return ResponseEntity.ok(new ResponseDto("Card created successfully.", HttpStatus.CREATED));
  }

  @Operation(description = "Retrieves card for mobile number")
  @ApiResponses({
      @ApiResponse(responseCode = "200",description = "content fetched successfully"),
      @ApiResponse(responseCode = "400",content =  @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
  })
  @GetMapping("/fetch")
  public ResponseEntity<ResponseDto> fetchCard(@RequestParam(name = "mobileNumber") @Size(min=10,max=10) @Schema(example = "1234567890") String mobileNumber) {
    CardsDto cardsDto=cardsService.getCard(mobileNumber);
    return ResponseEntity.ok(new ResponseDto(cardsDto, HttpStatus.OK));
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
    return new ResponseEntity<>(new ResponseDto(cardsContactInfoDto,HttpStatus.OK), HttpStatus.OK);
  }
}
