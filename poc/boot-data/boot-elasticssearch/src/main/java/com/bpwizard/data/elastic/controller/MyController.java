package com.bpwizard.data.elastic.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.data.elastic.model.Employee;
import com.bpwizard.data.elastic.service.EmployeeService;

@RestController
@RequestMapping(value = "/employee")
public class MyController {
	@Autowired
	EmployeeService eservice;

	/**
	 * Method to save the employees in the database.
	 * 
	 * @param myemployees
	 * @return
	 */
	@PostMapping(value = "/saveemployees")
	public String saveEmployee(@RequestBody List<Employee> myemployees) {
		eservice.saveEmployee(myemployees);
		return "Records saved in the db.";
	}

	/**
	 * Method to fetch all employees from the database.
	 * 
	 * @return
	 */
	@GetMapping(value = "/getall")
	public Iterable<Employee> getAllEmployees() {
		return eservice.findAllEmployees();
	}

	/**
	 * Method to fetch the employee details on the basis of designation.
	 * 
	 * @param designation
	 * @return
	 */
	@GetMapping(value = "/findbydesignation/{employee-designation}")
	public Iterable<Employee> getByDesignation(@PathVariable(name = "employee-designation") String designation) {
		return eservice.findByDesignation(designation);
	}
}