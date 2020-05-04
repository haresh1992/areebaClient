package com.test.spring.activiti.integration.controller;

import com.test.spring.activiti.integration.service.TestService;
import com.test.spring.activiti.integration.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@Autowired
	private TestService testService;

	@PostMapping("/startProcess")
	public String startProcess(@RequestBody final User user) throws ParseException {

		String result = testService.startProcess(user);

		if(result.equalsIgnoreCase("approved"))
			return "Your Application is Approved";
		else if(result.equalsIgnoreCase("rejected"))
			return "Your Application is Rejected";
		else
			return "Your Application is Invalid";
	}

}
