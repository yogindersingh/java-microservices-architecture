package com.learning.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountsController {

  @GetMapping("/helloworld")
  public String helloworld() {
    return "Hello World!";
  }
}
