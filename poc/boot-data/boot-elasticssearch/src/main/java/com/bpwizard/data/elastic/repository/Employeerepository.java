package com.bpwizard.data.elastic.repository;
import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.bpwizard.data.elastic.model.Employee;
 
@Repository
public interface Employeerepository extends ElasticsearchRepository<Employee, String> {
 
    /**
     * Method to fetch the employee details on the basis of designation by using Elastic-Search-Repository.
     * @param designation
     * @return
     */
    public List<Employee> findByDesignation(String designation);
 
}