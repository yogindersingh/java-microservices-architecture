package com.learning.spring_cloud_gateway_server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

  @GetMapping("/contactsupport")
  public String contactsupport() {
    return "Requested server is temporarily unavailable. please try again later";
  }

}
