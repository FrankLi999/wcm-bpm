## Learning partal
https://learn.openshift.com/
## Sample steps:

oc login 
oc new-project myproject

oc new-app registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift~https://github.com/fmarchioni/masterspringboot.git --context-dir=demo-docker --name=springboot-demo-docker

check > https://github.com/fmarchioni/masterspringboot/tree/master/demo-docker

 oc get services
oc expose svc/springboot-demo-docker


## fabric8 openshift build

buildStrategy configuration option (fabric8.build.strategy property): s2i (default) or docker

buildRecreate configuration option (property fabric8.build.recreate): buildConfig or bc, imageStream or is, all, none(default) 

mode configuration option (property fabric8.mode): kubernetes, openshift, auto (default), auto is the default. (Because of technical reasons, "kubernetes" is currently the default, but will change to "auto" eventually)
## install Codeready container
pre-depnendency libvirt and NetworkManager.

Linux Distribution	                  Installation command
Fedora                                sudo dnf install NetworkManager
Red Hat Enterprise Linux/CentOS       su -c 'yum install NetworkManager'
Debian/Ubuntu                         sudo apt install qemu-kvm libvirt-daemon \
                                         libvirt-daemon-system network-manager

sudo apt install qemu-kvm libvirt-daemon libvirt-daemon-system network-manager                                       

## Openshift CodeReady
cd openshift
crc start -n 8.8.8.8 -m 16384 -p pull-secret.txt

pull secret:  https://cloud.redhat.com/openshift/install/crc/installer-provisioned
view pwd:
crc console --credentials 
crc oc-env

https://github.com/Skatteetaten/openshift-reference-springboot-server/blob/master/README.md
https://github.com/Skatteetaten/openshift-reference-springboot-server
https://github.com/Skatteetaten/aurora-springboot2-starter
https://skatteetaten.github.io/aurora/
https://access.redhat.com/documentation/en-us/red_hat_jboss_fuse/6.3/html/fuse_integration_services_2.0_for_openshift/kube-spring-boot
https://access.redhat.com/documentation/en-us/reference_architectures/2017/html-single/spring_boot_microservices_on_red_hat_openshift_container_platform_3/index


build:
https://docs.openshift.com/container-platform/4.2/builds/triggering-builds-build-hooks.html

crc start -n 8.8.8.8 -m 16384 -p pull-secret.txt

https://manage.openshift.com/account/index

> add 'spec.template.spec.serviceAccountName: "openshift-demo"' to "src/resource/fabiric8:deployment.yml"
> create "src/resource/bootstrap.yml" to set up spring boot kubernetes

> oc import-image openjdk18 --from=registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift --confirm
> cd openshift
> oc create -f credentials-secret.yml
> oc create -f openshift-demo-serviceaccount.yml
> oc create -f configmap.yml
-- oc policy add-role-to-user view system:serviceaccount:$(oc project -q):default -n $(oc project -q)
-- oc policy add-role-to-user view system:serviceaccount:$(oc project -q):openshift-demo -n $(oc project -q)

> oc create rolebinding default-view --clusterrole=view --serviceaccount=wcm-bpm:default --namespace=wcm-bpm
> oc create rolebinding openshift-demo-view --clusterrole=view --serviceaccount=wcm-bpm:openshift-demo --namespace=wcm-bpm
> mvn clean install -DskipTests
> mvn fabric8:resource -DskipTests


-- push the container imager to openshift container registry
> mvn clean install -DskipTests

-- There is an issue with fabric8:deploy and fabric8:resource-apply with image="" in the generated openshift-demo-deploymentconfig.yml, 
-- for now manually add 'image:"image-registry.openshift-image-registry.svc:5000/wcm-bpm/openshift-demo:latest"' 
-- to openshift-demo-deploymentconfig.yml and use 'oc create -f' instead
-- mvn fabric8:build  -DskipTest
-- mvn clean fabric8:deploy -DskipTests
-- mvn clean fabric8:resource-apply -DskipTests
oc get pods -w

-- deploy the apps

> cd openshift
> oc create -f openshift-demo-deploymentconfig.yml
> oc create -f openshift-demo-service.yml
> oc create -f openshift-demo-route.yml



## MySQL Openshift

 https://github.com/sclorg/mysql-container/blob/master/8.0/root/usr/share/container-scripts/mysql/README.md
 https://docs.okd.io/latest/using_images/db_images/mysql.html
 
oc port-forward mysql-1-49t69 3306:3306

oc expose svc/mysql

GRANT ALL ON *.* TO 'wcmbpm'@'%';
flush privileges;  

## Loging on openshift

	https://docs.openshift.com/container-platform/4.2/logging/cluster-logging.html
	
	There are currently 5 different types of cluster logging
	components:
	
	> logStore - This is where the logs will be stored. The 
	  current implementation is Elasticsearch.
	
	  Role-based access control (RBAC) applied on the Elasticsearch indices enables 
	  the controlled access of the logs to the developers. Access to the indexes 
	  with the project.{project_name}.{project_uuid}.* format is restricted based on 
	  the permissions of the user in the specific project.
	  
	  The Cluster Logging Operator and companion Elasticsearch Operator 
	  Cluster Logging Custom Resource (CR)
	  
	> collection - This is the component that collects logs from 
	  the node, formats them, and stores them in the logStore. 
	  The current implementation is Fluentd.
	
	  Deployed as a DaemonSet in OpenShift Container Platform that deploys pods to each 
	  OpenShift Container Platform node.
	  
	> visualization - This is the UI component used to view 
	  logs, graphs, charts, and so forth. The current 
	  implementation is Kibana.
	
	> curation - This is the component that trims logs by age. 
	  The current implementation is Curator.
	
	  The Elasticsearch Curator tool performs scheduled maintenance operations on a global 
	  and/or on a per-project basis.
	> Event routing - This is the component forwards events 
	  to cluster logging. The current implementation is Event 
	  Router.
	  
	  The Event Router is a pod that forwards OpenShift Container Platform events to cluster 
	  logging. You must manually deploy Event Router.
	  
### EFK
>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
> Spring boot apps write logs STDOUT and picked up by Fluentd.
>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
> Set up EFK on openshift CRC container:
> https://docs.openshift.com/container-platform/4.2/logging/cluster-logging-deploying.html
> 
>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
Sample Jog4j2 config:
        
       <Properties>
          ...
        	<Property name="LOG_PATTERN">
            logdate=(%d{ISO8601}) thread=(%thread)) level=(%level) loggerclass=(%logger{36}) message=(%msg)%n
        	</Property>
    	</Properties>
       <Appenders>
        ...
		  <Console name="CONSOLE" target="SYSTEM_OUT" follow="true">
            <PatternLayout pattern="${LOG_PATTERN}"/>
        </Console>
        <Console name="JSONCONSOLE" target="SYSTEM_OUT" follow="true">
            <JSONLayout compact="false" complete="true"/>
        </Console>
        ...
       </Appenders> 
       ...
       
https://docs.openshift.com/container-platform/3.11/install_config/aggregate_logging.html

multiline logging:
https://access.redhat.com/solutions/2197831
https://itnext.io/multiline-logs-in-openshift-efk-stack-7a7bda4ed055
https://medium.com/@sbourazanis/openshift-logging-spring-boot-application-multiline-logs-handler-and-custom-log-fields-parser-e4e1a64cdc01



oc project openshift-logging
oc get pods -l component=fluentd

NAME READY STATUS RESTARTS AGE
logging-fluentd-8j6c9 1/1 Running 0 1d
logging-fluentd-mbpk8 1/1 Running 0 1d
logging-fluentd-q5z7f 1/1 Running 0 1d

Copy existing Fluentd plugins from one of the Fluentd pods to 
the master node:

$ oc exec logging-fluentd-8j6c9 -- bash -c ‘mkdir /extract; cp /etc/fluent/plugin/*.rb /extract;’
$ oc cp logging-fluentd-8j6c9:/extract /work/fluentd/plugin/
$ oc exec logging-fluentd-8j6c9 -- bash -c ‘rm -r /extract;’

Copy existing Fluentd configuration files from one of the Fluentd pods to the master node:

$ oc exec logging-fluentd-8j6c9 -- bash -c 'mkdir /extract; cp /etc/fluent/configs.d/user/*.conf /extract; cp /etc/fluent/configs.d/user/*.yaml /extract;'
$ oc cp logging-fluentd-8j6c9:/extract /work/fluentd/configs.d/user/
$ oc exec logging-fluentd-8j6c9 -- bash -c 'rm -r /extract;'

Backup existing Fluentd plugins and configuration files to be available for the uninstallation process:
cp -R /work/fluentd/ /work/fluentd_backup

https://developers.redhat.com/blog/2018/01/22/openshift-structured-application-logs/

### EFK on kube
https://technology.amis.nl/2019/04/23/using-vagrant-and-shell-scripts-to-further-automate-setting-up-my-demo-environment-from-scratch-including-elasticsearch-fluentd-and-kibana-efk-within-minikube/

 
### ELK
https://github.com/lbischof/openshift3-elk

###  log4jv2 Volume for test purpose

The simplest volume type is emptyDir, which is a temporary directory on a single machine.
Administrators may also allow you to request a persistent volume that is automatically 
attached to your pods.

## Tracing on Openshift

>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
>> 1. From openshift crc conatiner console, install Jaeger operator and create jaeger all-in-one deployment 
>> 2. Spring boot opentrace/Jaeger
>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
>> oc process -f https://raw.githubusercontent.com/jaegertracing/jaeger-openshift/master/all-in-one/jaeger-all-in-one-template.yml | oc create -f -
>> oc create route edge --service=jaeger-collector --port jaeger-collector-http --insecure-policy=Allow
>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
Jaeger operator:

	https://github.com/jaegertracing/jaeger-operator
	
Spring boot opentrace/Jaeger:
	
    https://www.hawkular.org/blog/2017/06/9/opentracing-spring-boot.html
	https://raw.githubusercontent.com/jaegertracing/jaeger-openshift/master/all-in-one/jaeger-all-in-one-template.yml
	
	<dependency>
	  <groupId>io.opentracing.contrib</groupId>
	  <artifactId>opentracing-spring-jaeger-cloud-starter</artifactId>
	</dependency>
	
	
	The "opentracing-spring-jaeger-web-starter" starter is convenience starter that
	includes both opentracing-spring-jaeger-starter and opentracing-spring-web-starter 
	This means that by including it, simple web Spring Boot microservices include all 
	the necessary dependencies to instrument Web requests / responses and send traces to
	Jaeger.
	
	The "opentracing-spring-jaeger-cloud-starter" starter is convenience starter that
	includes both opentracing-spring-jaeger-starter and opentracing-spring-cloud-starter
	This means that by including it, all parts of the Spring Cloud stack supported by
	Opentracing will be instrumented.
	
	
	If no settings are changed, spans will be reported to the UDP port 6831 of 
	localhost. The simplest way to change this behavior is to set the following 
	properties:
	
	UDP Sender:
	opentracing.jaeger.udp-sender.host=jaegerhost
	opentracing.jaeger.udp-sender.port=portNumber
	
	HTTP sender:
	opentracing.jaeger.http-sender.url = http://jaegerhost:portNumber/api/traces
	
	
	https://github.com/opentracing-contrib/java-spring-web

##Monitoring

https://docs.openshift.com/container-platform/4.2/monitoring/cluster-monitoring/about-cluster-monitoring.html
https://labs.consol.de/development/2018/01/19/openshift_application_monitoring.html

https://github.com/openshift/origin/tree/master/examples/prometheus
https://github.com/mrsiano/openshift-grafana
### Prometheus/Grafana

openshift deployment of prometheus and Grafana:
    https://github.com/simonmeggle/prometheus-openshift
    
	oc process prometheus-template | oc apply -f-
	
	oc process grafana-template -p NAMESPACE=sandbox-promapp | oc apply -f-
	
	#for prometheus to be able to read metrics from pods / services in the same project
	
	oc policy add-role-to-user view -z default
	
	#Add the the following annotation on the desired svc of app to be able to scrape
	
	prometheus.io/path: /actuator/prometheus
	prometheus.io/port: "8080"
	prometheus.io/scrape: "true"
	

prometheus: 
http://localhost:9090
query language: PromQL

graphana: http://localhost:3000
   admin/admin
   
spring boot:
    helpful links
	https://www.baeldung.com/micrometer
	http://www.bytesville.com/springboot-micrometer-prometheus-grafana/
	https://www.callicoder.com/spring-boot-actuator-metrics-monitoring-dashboard-prometheus-grafana/
	https://www.callicoder.com/spring-boot-actuator/
  maven:
  
 	 <dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-actuator</artifactId>
	 </dependency>
		
	 <!-- Micormeter core dependecy  -->
    <dependency>
            <groupId>io.micrometer</groupId>
            <artifactId>micrometer-core</artifactId>
    </dependency>
    <!-- Micrometer Prometheus registry  -->
    <dependency>
            <groupId>io.micrometer</groupId>
            <artifactId>micrometer-registry-prometheus</artifactId>
    </dependency>
    
  application.properties:
  	management.endpoint.metrics.enabled=true
	management.endpoints.web.exposure.include=*
	management.endpoints.web.exposure.include=health,info,metrics,prometheus
	management.endpoint.prometheus.enabled=true
	management.metrics.export.prometheus.enabled=true
	
  we can also enable cumulative histograms for SLAs and distribution percentiles by simply providing the below configurations.

	management.metrics.distribution.percentiles-histogram.http.server.requests=true
	management.metrics.distribution.sla.http.server.requests=50ms
  metric url: /actuator/prometheus