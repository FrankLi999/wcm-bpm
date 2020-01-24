package com.bpwizard.rules.drools.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bpwizard.rules.drools.service.CustomerService;
import com.bpwizard.rules.drools.model.Customer;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerServiceTest {

	@Autowired
	CustomerService service;

	@Test
	public void testDiscount() {
		Customer customer1 = new Customer(Customer.CustomerType.BUSINESS, 4);

		Customer customer2 = new Customer(Customer.CustomerType.INDIVIDUAL, 4);
		Customer customer3 = new Customer(Customer.CustomerType.INDIVIDUAL, 1);
		service.insertCustomer(customer1);
		service.insertCustomer(customer2);
		service.insertCustomer(customer3);
		assertEquals(20, customer1.getDiscount());
		assertEquals(15, customer2.getDiscount());
		assertEquals(5, customer3.getDiscount());
	}

}