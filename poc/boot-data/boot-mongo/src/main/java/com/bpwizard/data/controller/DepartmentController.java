package com.bpwizard.data.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.data.model.Department;
import com.bpwizard.data.repo.DepartmentRepository;

@RestController
@RequestMapping("/mongo/dept")
public class DepartmentController {
	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@PostMapping
	public Department createDept(@RequestBody Department department) {
		departmentRepository.save(department);
		return department;
	}

	@GetMapping
	public List<Department> listDepts() {
		return departmentRepository.findAll();
	}

	@PutMapping("/{deptId}")
	public Department updateDept(@RequestBody Department department, @PathVariable String deptId) {
		department.setId(deptId);
		departmentRepository.save(department);
		return department;
	}

	@DeleteMapping("/{deptId}")
	public String deleteDept(@PathVariable String deptId) {
		departmentRepository.deleteById(deptId);
		return deptId;
	}

	@GetMapping("/{deptName}")
    public List<Department> findDeptByName(@PathVariable String deptName) {
        return departmentRepository.findDepartmentByName(deptName);
    }
	
	@GetMapping("{name}/emp")
    public Department listDept(@PathVariable String name){
        return departmentRepository.findDepartmentByEmployeeName(name);
    }
	
	@PostMapping("/template")
	public Department save(Department department) {
		mongoTemplate.save(department);
		return department;
	}

	@GetMapping("/template")
	public List<Department> findAll() {
		return mongoTemplate.findAll(Department.class);
	}

	@PutMapping("/template/{deptId}")
	public Department update(Department department) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(department.getId()));
		Update update = new Update();
		update.set("name", department.getName());
		update.set("description", department.getDescription());
		return mongoTemplate.findAndModify(query, update, Department.class);
	}

	@DeleteMapping("/template/{deptId}")
	public void deleteById(String deptId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(deptId));
		mongoTemplate.remove(query, Department.class);
	}
	
	@GetMapping("/template/{deptName}")
    public List<Department> findDeptByNameWithTemplate(@PathVariable String deptName) {
		Query query = new Query();
        query.addCriteria(Criteria.where("name").is(deptName));
        return mongoTemplate.find(query, Department.class);
    }
	
	@GetMapping("/template/{name}/emp")
    public Department listDeptWithTemplate(@PathVariable String name){
        return departmentRepository.findDepartmentByEmployeeName(name);
    }
}
