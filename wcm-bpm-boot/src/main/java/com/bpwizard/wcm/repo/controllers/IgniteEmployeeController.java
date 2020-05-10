package com.bpwizard.wcm.repo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ignite")
public class IgniteEmployeeController {

	@Autowired
	private IgniteEmployeeService employeeService;

	@GetMapping("/employee/{id}")
	public IgniteEmployee findEmployeeById(@PathVariable String id) {
		System.out.println("Searching by ID  : " + id);
		return employeeService.getEmployeeByID(id);
	}

	@RequestMapping(value = "/employee/{id}/{name}", method = RequestMethod.PUT)
	public IgniteEmployee findEmployeeById(@PathVariable String id, @PathVariable String name) {
		System.out.println("Updating by ID  : " + id);
		return employeeService.updateEmployee(id, name);
	}

	@RequestMapping(value = "employee/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteEmployeeById(@PathVariable String id) {
		System.out.println("Updating by ID  : " + id);
		employeeService.deleteEmployee(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
