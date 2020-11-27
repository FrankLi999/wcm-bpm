## Camunda dependencies
  camunda-release-parent (3.7)
    camunda-bpm-release-parent (1.0.0)
    
	  camunda-commons-logging
	  camunda-commons-utils
	  camunda-commons-testing
	  
	  camunda-xml-model
	  camunda-bpmn-model
	  camunda-cmmn-model
	  camunda-dmn-model
	  
	  
	  
	  camunda-engine
	  camunda-engine-dmn
	  camunda-engine-feel-api
	  camunda-engine-feel-juel
	  camunda-commons-typed-values
	  engine-plugins
	  		camunda-identity-ldap
	  		camunda-engine-plugin-spin
	  		camunda-engine-plugin-connect
	  camunda-engine-rest-jaxrs2
	  camunda-engine-spring
	  
	  
	  camunda-bpmn-spring-boot-starter 
	  camunda-bpmn-spring-boot-starter-rest
  
  
  		camunda-bpm-spring-boot-starter-test
  
  		camunda-bpm-assert
  		camunda-bpm-assert-scenario
  		camunda-bpm-process-test-coverage
  
  
      camunda-spin-core
      camunda-spin-dataformat-json-jackson
  
     
	   camunda-engine-connect-core
	   camunda-connect-http-client
  
## Engine build

  <profile>
      <!-- profile for running process-engine unit tests in QA -->
      <id>camunda-boot</id>
	  <activation>
        <activeByDefault>true</activeByDefault>
      </activation>
      <modules>
	    <!-- <module>qa</module> -->
	    <module>typed-values</module>
        <module>engine-dmn</module>


       <module>engine</module>
       <module>engine-spring</module>
       <module>engine-rest</module>

		<module>engine-plugins</module>

       <module>distro/sql-script</module>
       <module>database</module>
       <module>parent</module>
       <module>bom</module>
     </modules>
  </profile> 