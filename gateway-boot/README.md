Derived from https://github.com/dromara/soul.git

1. gateway admin


> wget  https://yu199195.github.io/jar/soul-admin.jar
> java -jar soul-admin.jar --spring.datasource.url="jdbc:mysql://mysql:3306/soul?allowPublicKeyRetrieval=true&useSSL=false&autoReconnect=true&nullNamePatternMatchesAll=true" --spring.datasource.username=wcmbpm  --spring.datasource.password=P@ssw0rd

> http://localhost:9095/index.html
  admin/123456
  
2. create a gateway

   <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webflux</artifactId>
        <version>2.2.2-RELEASE</version>
  </dependency>

  <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
        <version>2.2.2-RELEASE</version>
  </dependency>

  <!--soul gateway start-->
  <dependency>
        <groupId>org.dromara</groupId>
        <artifactId>soul-spring-boot-starter-gateway</artifactId>
        <version>2.2.0</version>
  </dependency>
  
   <!--soul data sync start use websocket-->
   <dependency>
        <groupId>org.dromara</groupId>
        <artifactId>soul-spring-boot-starter-sync-data-websocket</artifactId>
        <version>2.2.0</version>
   </dependency>