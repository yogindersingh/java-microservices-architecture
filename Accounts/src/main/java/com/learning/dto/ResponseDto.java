package com.learning.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@Schema(name = "Response")
public class ResponseDto {
  Object body;

  @Schema(enumAsRef = true)
  HttpStatus status;
}
