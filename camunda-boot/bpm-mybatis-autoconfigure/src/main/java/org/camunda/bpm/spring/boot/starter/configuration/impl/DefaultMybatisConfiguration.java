package org.camunda.bpm.spring.boot.starter.configuration.impl;

import org.apache.ibatis.session.SqlSessionFactory;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.CamundaMybatisConfiguration;

public class DefaultMybatisConfiguration extends AbstractCamundaConfiguration
		implements CamundaMybatisConfiguration {
	
    private SqlSessionFactory sqlSessionFactory;
	
    public DefaultMybatisConfiguration(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;	
    }
	
    @Override
	public void preInit(SpringProcessEngineConfiguration configuration) {
		SpringProcessEngineConfiguration.cachedSqlSessionFactory = sqlSessionFactory;
		configuration.setUseSharedSqlSessionFactory(true);
	}
}
