package com.bpwizard.bpm.drools.service;

import static junit.framework.TestCase.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bpwizard.bpm.drools.model.Customer;

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
		int discount1 = service.customerDiscount(customer1);
		int discount2 = service.customerDiscount(customer2);
		int discount3 = service.customerDiscount(customer3);
		assertEquals(20, discount1);
		assertEquals(15, discount2);
		assertEquals(5, discount3);
	}

}