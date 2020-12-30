package com.bpwizard.bpm.config;
import org.apache.ibatis.session.SqlSessionFactory;
import org.camunda.bpm.spring.boot.starter.CamundaBpmAutoConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.impl.DefaultMybatisConfiguration;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;


// @Configuration 
@AutoConfigureAfter({MybatisAutoConfiguration.class })
@AutoConfigureBefore({CamundaBpmAutoConfiguration.class})
public class BpmMybatisAutoConfig {

	@Autowired
	// @Qualifier("camundaSqlSessionFactory")
	protected SqlSessionFactory sqlSessionFactory;
	
    @Bean
    @DependsOn("sqlSessionFactory")
    @ConditionalOnMissingBean
    public DefaultMybatisConfiguration mybatisConfiguration() {
        return new DefaultMybatisConfiguration(sqlSessionFactory);
	}
}
