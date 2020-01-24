package com.bpwizard.data.elastic.service;

import java.util.List;

import com.bpwizard.data.elastic.model.Employee;

public interface EmployeeService {
	public void saveEmployee(List<Employee> employees);
	Iterable<Employee> findAllEmployees();
	List<Employee> findByDesignation(String designation);
}
