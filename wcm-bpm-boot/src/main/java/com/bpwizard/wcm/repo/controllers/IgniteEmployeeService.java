package com.bpwizard.wcm.repo.controllers;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import java.util.List;

@Service
public class IgniteEmployeeService {

    private List<IgniteEmployee> employeeList = new ArrayList<>();

    @Cacheable(cacheNames="wcm",key="#id")
    public IgniteEmployee getEmployeeByID(String id)
    {
        try
        {
            System.out.println(" Will Sleep for 5 Seconds");
            Thread.sleep(1000*5);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        IgniteEmployee employee = new IgniteEmployee(id,"Suman Das");
        employeeList.add(employee);
        return employee;
    }

    @CachePut(cacheNames="wcm", key="#id" , unless="#result==null")
    public IgniteEmployee updateEmployee(String id,String name) {
        for(IgniteEmployee e : employeeList){
            if(e.getId().equalsIgnoreCase(id)) {
                e.setName(name);
               return e;
            }
        }
        return null;
    }

    @CacheEvict(value = "wcm", key="#id")
    public void deleteEmployee(String id){
        employeeList.removeIf(e -> e.getId().equalsIgnoreCase(id));
    }
}