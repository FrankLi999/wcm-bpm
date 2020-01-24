# Reference
https://examples.javacodegeeks.com/enterprise-java/spring/boot/spring-boot-elasticsearch-tutorial/

# Test URL

// Get all employees
http://localhost:8080/employee/getall
 
// Find employee by designation
http://localhost:8080/employee/findbydesignation/developer
 
// Create new employee
http://localhost:8080/employee/saveemployees

# Download ElasticSearch and install a local service

download it from https://www.elastic.co/downloads/elasticsearch.

wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.4.2-linux-x86_64.tar.gz 
tar -xcvf elasticsearch-7.4.2-linux-x86_64.tar.gz 

start ElasticSearch server:
$cd /home/frank/tools/elasticsearch-7.4.2
$bin/elasticsearch 

access http://localhost:9200/ 