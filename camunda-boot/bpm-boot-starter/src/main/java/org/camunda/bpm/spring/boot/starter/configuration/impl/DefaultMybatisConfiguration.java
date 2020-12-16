package org.camunda.bpm.spring.boot.starter.configuration.impl;

import org.apache.ibatis.session.SqlSessionFactory;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.CamundaMybatisConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultMybatisConfiguration extends AbstractCamundaConfiguration
		implements CamundaMybatisConfiguration {

	@Autowired
	protected SqlSessionFactory sqlSessionFactory;
	

	@Override
	public void preInit(SpringProcessEngineConfiguration configuration) {
		SpringProcessEngineConfiguration.cachedSqlSessionFactory = sqlSessionFactory;
		configuration.setUseSharedSqlSessionFactory(true);
	}
}
