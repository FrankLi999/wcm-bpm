package com.bpwizard.wcm.repo.context;

import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContext implements ApplicationContextAware {
	private static ApplicationContext applicationContext;
	public void setApplicationContext(ApplicationContext applicationContext) {
		SpringContext.applicationContext = applicationContext;
	}
	
	public static DataSource getDataSource() throws BeansException {
		return (DataSource) applicationContext.getBean("modeshapeDataSource");
	}
}
