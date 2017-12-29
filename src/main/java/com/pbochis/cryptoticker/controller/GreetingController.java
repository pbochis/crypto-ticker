package com.pbochis.cryptoticker.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {

	private final AtomicLong counter = new AtomicLong();

	@RequestMapping("/")
	public Greeting greet(@RequestParam(value = "name", defaultValue = "default") String name) {
		return new Greeting(2, name);
	}

	@RequestMapping("/custom")
	public Greeting asdf(@RequestParam(value = "name", defaultValue = "custom") String name) {
		return new Greeting(4, name);
	}

}
