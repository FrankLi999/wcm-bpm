## Issues - BPM-UI

   > cors config for logging in from wcm-authoring:3009.  
   > wcm-authoring connecting to render server - > load wcm system from a library other than the default camunda
      for now:
            1) ./wcm-authoring/src/environments/environment.tsenvironment.prod.ts
            ```
                  // wcmLibrary: "camunda",
                  // wcmSiteConfig: "bpm",
                  wcmLibrary: "mysite",
                  wcmSiteConfig: "mysite",
                  oauth2RedirectUrl: "http://wcm-authoring:3009/oauth2/redirect",
                  // wcmApiBaseUrl: "",
                  // bpmApiBaseUrl: ""
                  // wcmApiBaseUrl: "http://wcm-server.bpwizard.com:28080",
                  // wcmApiBaseUrl: "http://wcm-server.bpwizard.com:28081",
                  wcmApiBaseUrl: "http://wcm-server.bpwizard.com:28082",
                  bpmApiBaseUrl: "http://bpm-server.bpwizard.com:28081"
            ```
            2. packagon.json
                  "start-wcm-authoring": "ng serve --port 3009 --host=wcm-authoring --project wcm-authoring --proxy-config proxy.conf-mysite.json",

   > proxy-config.json, proxy-config-bpm.json,  proxy-config-mysite.json, 
   
   > com.bpwizard.wcm.repo.rest.service.WcmEventService
      addCNDEvent()
   >   JsonNodeSyndicationHandler
       skip libraryFolder type
   > Syndication events: same miliseconds
## Start the WCM dev enviornments
      >         npm install http-server -g
      > build angular apps
          ./wcm_bpm/ui/boot/angular

          NOTE: for now, don't build libraries, build apps directly.

            > npm run copy-styles
            > npm run copy-wcm-styles
            > mvn clean install -Dangular-project=bpm-ui
            > mvn clean install -Dangular-project=wcm-authoring
            > mvn clean install -Dangular-project=mysite

      > db user demo/P@ssw0rd
      > run setup app to set up mysql database.
            > wcm clean install -DskipTests
            ./wcm-bpm/setup

            option 1: From within Eclipse,
                  -DWCM_DB_HOST=localhost:3306 -DBPM_DB_HOST=localhost:3306 -DACCOUNT_DB_HOST=localhost:3306 -DGATEWAY_DB_HOST=localhost:3306

            option 2:

                  mvn spring-boot:run -Dspring-boot.run.arguments="--spring.main.banner-mode=OFF" -Dspring-boot.run.jvmArguments="-DWCM_DB_HOST=localhost:3306 -DBPM_DB_HOST=localhost:3306 -DACCOUNT_DB_HOST=localhost:3306 -DGATEWAY_DB_HOST=localhost:3306"

      > run bpm server:
            ./wcm_bpm/ui/boot/bpm_server
            > wcm clean install -DskipTests

            npm run build --project bpm-ui 
            http-server dist/bpm-ui --port 4009
            http://bpm-ui:4009
            
            bpm server with wcm bpm site
            db: bpm/bpm_site/account

            bpm-server:
            4009/28081:

            -Djava.security.egd=file:/dev/./urandom-28081 -Dspring.profiles.active=dev -Djasypt.encryptor.password=password -DROOT_LOG_LEVEL=WARN -DAPP_LOG_LEVEL=WARN -DSQL_LOG_LEVEL=WARN -DBPM_PORT=28081 -Dlog.root.dir=/var/spring-logs/bpm-server-28081 -Dspring.jta.log-dir=/home/ubuntu/var/camunda/transaction-logs-28081 -Dglowroot.agent.port=6050 -javaagent:/home/ubuntu/tools/glowroot/glowroot-0.13.6/glowroot.jar

            -Djava.security.egd=file:/dev/./urandom-4009 -Dspring.profiles.active=dev -Djasypt.encryptor.password=password -DROOT_LOG_LEVEL=WARN -DAPP_LOG_LEVEL=WARN -DSQL_LOG_LEVEL=WARN -DBPM_PORT=4009 -Dlog.root.dir=/var/spring-logs/bpm-server-4009 -Dspring.jta.log-dir=/home/ubuntu/var/camunda/transaction-logs-4009 -Dglowroot.agent.port=6050 -javaagent:/home/ubuntu/tools/glowroot/glowroot-0.13.6/glowroot.jar

      wcm-authoring-server:
            ./wcm_bpm/ui/boot/wcm_authoring_server
            > wcm clean install -DskipTests

            or 

              npm run build --project wcm-authoring
              http-server dist/wcm-authoring --port 3009
              http://wcm-authoring:3009
                  

            port 3009/28080, for wcm-authoring, wcm_authoring/account
            -Djava.security.egd=file:/dev/./urandom-28080 -Dspring.profiles.active=dev -Djasypt.encryptor.password=password -DROOT_LOG_LEVEL=WARN -DAPP_LOG_LEVEL=WARN -DSQL_LOG_LEVEL=WARN -DWCM_PORT=28080 -Dlog.root.dir=/home/ubuntu/var/spring-logs/wcm-authoring-server-28080 -Dspring.jta.log-dir=/home/ubuntu/var/modeshape/transaction-logs-28080 -Dglowroot.agent.port=5000 -javaagent:/home/ubuntu/tools/glowroot/glowroot-0.13.6/glowroot.jar

            -Djava.security.egd=file:/dev/./urandom-3009 -Dspring.profiles.active=dev -Djasypt.encryptor.password=password -DROOT_LOG_LEVEL=WARN -DAPP_LOG_LEVEL=WARN -DSQL_LOG_LEVEL=WARN -DWCM_PORT=3009 -Dlog.root.dir=/home/ubuntu/var/spring-logs/gateway-admin-boot-3009 -Dspring.jta.log-dir=/home/ubuntu/var/modeshape/transaction-logs-3009 -Dglowroot.agent.port=5000 -javaagent:/home/ubuntu/tools/glowroot/glowroot-0.13.6/glowroot.jar

            preload content:
                  http://localhost:3009/core/api/login
                  {
                  "email": "admin@example.com",
                  "password":"admin!"
                  }

                  http://localhost:3009/wcm/api/import/preload

                  Authorization: "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJhdXRoIiwic3ViIjoiYWRtaW5AZXhhbXBsZS5jb20iLCJleHAiOjE2MTczMjk2OTAsImJwdy1pYXQiOjE2MTY0NjU2OTA4NzZ9.egy_8xNRObtWAmD1qcRaBZUfy3H2yTClcrFHR74DdPE"

      wcm server:
         ./wcm_bpm/ui/boot/wcm_server
         port 6009, for mysite, wcm_site/account

              npm run build --project mysite
              http-server dist/mysite --port 6009
              http://mysite:6009
                  6009/28082
            -Djava.security.egd=file:/dev/./urandom-28082 -Dspring.profiles.active=dev -Djasypt.encryptor.password=password -DROOT_LOG_LEVEL=WARN -DAPP_LOG_LEVEL=WARN -DSQL_LOG_LEVEL=WARN -DWCM_PORT=28082 -Dlog.root.dir=/home/ubuntu/var/spring-logs/wcm-server-28082 -Dspring.jta.log-dir=/home/ubuntu/var/modeshape/transaction-logs-28082 -Dglowroot.agent.port=7000 -javaagent:/home/ubuntu/tools/glowroot/glowroot-0.13.6/glowroot.jar

            -Djava.security.egd=file:/dev/./urandom-6009 -Dspring.profiles.active=dev -Djasypt.encryptor.password=password -DROOT_LOG_LEVEL=WARN -DAPP_LOG_LEVEL=WARN -DSQL_LOG_LEVEL=WARN -DWCM_PORT=6009 -Dlog.root.dir=/home/ubuntu/var/spring-logs/wcm-server-6009 -Dspring.jta.log-dir=/home/ubuntu/var/modeshape/transaction-logs-6009 -Dglowroot.agent.port=5050 -javaagent:/home/ubuntu/tools/glowroot/glowroot-0.13.6/glowroot.jar  

start syndication:
      rest based:

-------------------------------------------------------------------------------------------------------
resp api:
      http://localhost:28080/core/api/login
            POST: {"email":"admin@example.com","password":"admin!"}

      http://localhost:28080/wcm/api/import/preload
      post
            header:
                  Authorization

      http://wcm-server.bpwizard.com:20080/wcm/api/wcm-server
      http://wcm-site.bpwizard.com:20082/wcm/api/wcm-server
            id: 1
            {
                  "host": "wcm-authoring",
                  "port": "28080"
            }
            id: 2
            {
                  "host": "mysite",
                  "port": "28082"
            },
            
      
      http://wcm-server.bpwizard.com:28080/wcm/api/syndicator
      http://wcm-site.bpwizard.com:20082/wcm/api/syndicator
            POST: 
            {
                  "repository": "bpwizard", 
                  "workspace": "default",
                  "libraries": ["design", "mysite", "camunda", "apigateway"],
                  "collector": {
                        "id": "2"
                  }
            }
      http://wcm-server.bpwizard.com:28080/wcm/api/collector
      http://wcm-site.bpwizard.com:20082/wcm/api/collector
            POST:
            {
                  "syndicator": {
                        "id": "1"
                  }
            }
      http://wcm-server.bpwizard.com:28080/wcm/api/syndicator/syndicate
      POST:
            {
                  "syndicationId": 1,
                  "endTime": 17630763373342
            }

            http://mysite:28082/wcm/api/import/bpwizard/default/nodetypes
      
------------------------------------------------------------------------------------------------------------
set up:
      -DWCM_DB_HOST=localhost:3306 -DBPM_DB_HOST=localhost:3306 -DACCOUNT_DB_HOST=localhost:3306 -DGATEWAY_DB_HOST=localhost:3306

      mvn spring-boot:run -Dspring-boot.run.arguments="--spring.main.banner-mode=OFF" -Dspring-boot.run.jvmArguments="-DWCM_DB_HOST=localhost:3306 -DBPM_DB_HOST=localhost:3306 -DACCOUNT_DB_HOST=localhost:3306 -DGATEWAY_DB_HOST=localhost:3306"

wcm-authoring-server:

-Djava.security.egd=file:/dev/./urandom-28080 -Dspring.profiles.active=dev 
      -Djasypt.encryptor.password=password -DROOT_LOG_LEVEL=WARN -DAPP_LOG_LEVEL=WARN -DSQL_LOG_LEVEL=WARN
      -DWCM_PORT=28080 -Dlog.root.dir=/home/ubuntu/var/spring-logs/gateway-admin-boot-28080
      -Dspring.jta.log-dir=/home/ubuntu/var/modeshape/transaction-logs-28080 -Dglowroot.agent.port=5000 
      -javaagent:/home/ubuntu/tools/glowroot/glowroot-0.13.6/glowroot.jar

-Djava.security.egd=file:/dev/./urandom-28080 -Dspring.profiles.active=dev -Dspring.jta.log-dir=/home/ubuntu/var/modeshape/transaction-logs-28080 -DWCM_PORT=28080

program argument:
      -Dspring-boot.run.argument: passing command line arguments
VM arguments: spring-boot.run.jvmArguments
      -Drun.arguments: overriding system properties

      mvn spring-boot:run -Dspring-boot.run.arguments="--spring.main.banner-mode=OFF" -Dspring-boot.run.jvmArguments="-Djava.security.egd=file:/dev/./urandom-28080 -Dspring.profiles.active=dev -Dspring.jta.log-dir=/home/ubuntu/var/modeshape/transaction-logs-28080 -DWCM_PORT=28080" -P mysql
      java -Djava.security.egd=file:/dev/./urandom-28080 -Dspring.profiles.active=dev -Dspring.jta.log-dir=/home/ubuntu/var/modeshape/transaction-logs-28080 -DWCM_PORT=28080 -jar  wcm-authoring-server-0.0.1-SNAPSHOT-LOCAL.jar

debug mode:
      mvn spring-boot:run -Drun.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000 -Djava.security.egd=file:/dev/./urandom-28080 -Dspring.profiles.active=dev -Dspring.jta.log-dir=/home/ubuntu/var/modeshape/transaction-logs-28080 -DWCM_PORT=28080" -P mysql


-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8000

mvn spring-boot:run -Dspring-boot.run.arguments="--spring.main.banner-mode=OFF" -Dspring-boot.run.jvmArguments="-Dspring.profiles.active=dev -Dspring.jta.log-dir=/home/ubuntu/var/modeshape/transaction-logs-3009 -DWCM_PORT=3009" -P mysql

authoring:
      mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.profiles.active=dev -Dspring.jta.log-dir=/home/ubuntu/var/modeshape/transaction-logs-3009 -DWCM_PORT=3009" -P mysql
mysite:
      mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.profiles.active=dev -Dspring.jta.log-dir=/home/ubuntu/var/modeshape/transaction-logs-6009 -DWCM_PORT=6009" -P mysql
      java -Dspring.profiles.active=dev -Dspring.jta.log-dir=/home/ubuntu/var/modeshape/transaction-logs-6009 -DWCM_PORT=6009 -jar wcm-server-0.0.1-SNAPSHOT-LOCAL.jar
bpm-server:

-Djava.security.egd=file:/dev/./urandom-4009 -Dspring.profiles.active=dev 
      -Djasypt.encryptor.password=password -DROOT_LOG_LEVEL=WARN -DAPP_LOG_LEVEL=WARN -DSQL_LOG_LEVEL=WARN
      -DBPM_PORT=4009 -Dlog.root.dir=/var/spring-logs/bpm-server-4009
      -Dspring.jta.log-dir=/home/ubuntu/var/camunda/transaction-logs-4009 -Dglowroot.agent.port=6050 
      -javaagent:/home/ubuntu/tools/glowroot/glowroot-0.13.6/glowroot.jar
	  

mvn spring-boot:run -Dspring-boot.run.arguments="--spring.main.banner-mode=OFF" -Dspring-boot.run.jvmArguments="-Dspring.profiles.active=dev -DBPM_PORT=4009 -Dspring.jta.log-dir=/home/ubuntu/var/camunda/transaction-logs-4009"

      

bpm-server: 
 java -Djava.security.egd=file:/dev/./urandom-28081 -Dspring.profiles.active=dev -Dspring.jta.log-dir=/home/ubuntu/var/camunda/transaction-logs-4009 -DBPM_PORT=28081 -jar bpm-server-0.0.1-SNAPSHOT-LOCAL.jar 

-Djava.security.egd=file:/dev/./urandom-28083 -Dspring.profiles.active=dev 
      -Djasypt.encryptor.password=password -DROOT_LOG_LEVEL=WARN -DAPP_LOG_LEVEL=WARN -DSQL_LOG_LEVEL=WARN
      -DWCM_PORT=28083 -Dlog.root.dir=c:/var/spring-logs/bpm-boot-ui-28083
      -Dspring.jta.log-dir=c:/var/modeshape/transaction-logs-28083 -Dglowroot.agent.port=5003 
      -javaagent:c:/tools/glowroot/glowroot-0.13.6/glowroot.jar
	  
	  
authoring-boot-ui

-Djava.security.egd=file:/dev/./urandom-28082 -Dspring.profiles.active=dev 
      -Djasypt.encryptor.password=password -DROOT_LOG_LEVEL=WARN -DAPP_LOG_LEVEL=WARN -DSQL_LOG_LEVEL=WARN
      -DWCM_PORT=28082 -Dlog.root.dir=/var/spring-logs/authoring-boot-ui-28082
      -Dspring.jta.log-dir=/home/ubuntu/var/modeshape/transaction-logs-28082 -Dglowroot.agent.port=5002 
      -javaagent:/home/ubuntu/tools/glowroot/glowroot-0.13.6/glowroot.jar	  

mvn spring-boot:run -Dspring-boot.run.arguments="--spring.main.banner-mode=OFF" -Dspring-boot.run.jvmArguments="-Dspring.profiles.active=dev -DBPM_PORT=28082 -Dspring.jta.log-dir=/home/ubuntu/var/modeshape/transaction-logs-28082" -P mysql      


camel-syndicator:

    -Djava.security.egd=file:/dev/./urandom-18090 -Dspring.profiles.active=dev -Dspring.jta.log-dir=/home/ubuntu/var/modeshape/transaction-logs-18090 -DWCM_PORT=18090 -DWCM_DB_HOST_PORT=localhost:3306