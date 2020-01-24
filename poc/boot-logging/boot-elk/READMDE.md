# Reference
https://www.javainuse.com/spring/springboot-microservice-elk



# Download ElasticSearch and install a local service

download it from https://www.elastic.co/downloads/elasticsearch.

wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.4.2-linux-x86_64.tar.gz 
tar -xcvf elasticsearch-7.4.2-linux-x86_64.tar.gz 

start ElasticSearch server:
$cd /home/frank/tools/elasticsearch-7.4.2
$bin/elasticsearch 

access http://localhost:9200/ 

# 

download from https://www.elastic.co/downloads/kibana
wgwet https://artifacts.elastic.co/downloads/kibana/kibana-7.4.2-linux-x86_64.tar.gz

tar -xzvf kibana-7.4.2-linux-x86_64.tar.gz
## Set elastic host

Open config/kibana.yml in an editor
Set elasticsearch.hosts to point at your Elasticsearch instance
    elasticsearch.url: "http://localhost:9200"
## Start Kibana	
bin/kibana
http://localhost:5601 

# Download Logstash and install a local service
https://www.elastic.co/downloads/logstash

wget https://artifacts.elastic.co/downloads/logstash/logstash-7.4.2.tar.gz
tar -xzvf logstash-7.4.2.tar.gz

## prepare config

Run bin/logstash -f logstash.conf

logstash1.conf content:
input {
    file {
        path => "/home/frank/tools/elk-logs/spring-logback/*.log"
        codec => "json"
        type => "logback"
    }
}

output {
    if [type]=="logback" {
         elasticsearch {
             hosts => [ "localhost:9200" ]
             index => "logback-%{+YYYY.MM.dd}"
        }
    }
}

logstash2.conf:
input {

  tcp {

    port => 5000

    codec => json

  }
  
  gelf-pipeline.conf:
  
  input {
  gelf {
    id => "gelf"
    use_udp => false
    use_tcp => true
  }
}

filter {
  prune {
   blacklist_names => [ "source_host", "className", "facility" ]
  }
  mutate {
    # Sets the default value for application, in case someone forgot to set it in their
    # Gelf configuration
    coerce => { "application" => "unknown_application" }
  }
}

output {
  # (Un)comment for debugging purposes
  # stdout { codec => rubydebug }
  elasticsearch {
        hosts => ["http://my.elastic.host:9200/"]
        index => "app-%{application}-%{+YYYY.MM.dd}"
  }
}

}

output {

  elasticsearch {

    hosts => ["http://elasticsearch:9200"]

    index => "micro-%{appname}"

  }

}
