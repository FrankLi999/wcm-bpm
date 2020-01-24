package com.bpwizard.data.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.bpwizard.data.model.Department;

@Repository
public interface DepartmentRepository extends MongoRepository<Department, String> {

    @Query(value = "{'employees.name': ?0}", fields = "{'employees' : 0}")
    Department findDepartmentByEmployeeName(String empName);

    List<Department> findDepartmentByName(String name);
}