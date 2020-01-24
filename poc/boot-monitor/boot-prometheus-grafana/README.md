# Copied from
https://github.com/hellosatish/monitoring
http://dzone.com/articles/monitoring-using-spring-boot-2-prometheus-amp-graf
https://dzone.com/articles/monitoring-using-spring-boot-2-prometheus-and-graf
https://dzone.com/articles/monitoring-using-spring-boot-20-prometheus-and-gra
# Monitoring
To explore monitoring using Prometheus and Grafana. 

# Set Up Prometheus

https://www.howtoforge.com/tutorial/monitor-ubuntu-server-with-prometheus/


wget https://s3-eu-west-1.amazonaws.com/deb.robustperception.io/41EFC99D.gpg | sudo apt-key add -

sudo apt-get update -y

sudo apt-get install prometheus prometheus-node-exporter prometheus-pushgateway prometheus-alertmanager -y

sudo systemctl start prometheus
sudo systemctl enable prometheus
sudo systemctl is-enabled prometheus
sudo systemctl status prometheus
sudo systemctl disable prometheus


http://192.168.0.168:9090'

# config prometheus
download prometheus from: https://github.com/prometheus/prometheus/releases/download/v2.13.1/prometheus-2.13.1.linux-amd64.tar.gz

untar it into ~/tools/prometheus

./etc/prometheus/prometheus.yml

add a config file: person-app.yml

#Global configurations

global:

  scrape_interval:     5s # Set the scrape interval to every 5 seconds.

  evaluation_interval: 5s # Evaluate rules every 5 seconds.

scrape_configs:

  - job_name: 'person-app'

    metrics_path: '/actuator/prometheus'

    static_configs:

      - targets: ['localhost:9000']


Run prometheus as: 

    ./prometheus --config.file=person-app.yml

# Set Up Grafana

standalone binary:
    wget https://dl.grafana.com/oss/release/grafana-6.4.3.linux-amd64.tar.gz
    tar -zxvf grafana-6.4.3.linux-amd64.tar.gz

or

wget https://dl.grafana.com/oss/release/grafana_6.4.3_amd64.deb

from file explorer double click the deb file.
or 

sudo wget -q -O - https://packages.grafana.com/gpg.key | apt-key add -
sudo apt-get update
sudo apt-get install grafana

$ sudo systemctl start grafana-server
$ sudo systemctl status grafana-server
sudo vi /etc/grafana/grafana.ini

start grafana server:
    cd /home/frank/tools/grafana-6.4.3/bin
    > ./grafana-server
       log in as admin/admin


sudo dpkg -i grafana_6.4.3_amd64.deb

# Add Prometheus as a Data Source in Grafana

https://www.callicoder.com/spring-boot-actuator-metrics-monitoring-dashboard-prometheus-grafana/

> Log into Grafana with the username and password configured 
  (the default is admin/admin).
> Click the gear icon in the left sidebar and from the menu select 
  "Data Sources." This will list all the configured data sources 
  (if you have configured any).
> lick on "Add Data Source." This will open a page to add a data source.

    o Give a suitable name to this new data source, as this will be used 
      while creating visualizations. I am using "prometheus-local."
    o Select Prometheus in the "type" drop down.
    o The URL shall be "http://localhost:9090" as we have Prometheus 
      running on local host on port 9090.
    o Fill other details if you have any security or HTTP related settings.
    o Click "Save & Test."
    o If Grafana is able to make connections to Prometheus instance with the 
      details provided, then you will get a message saying "Data source is working." 
      If you get any errors, review your values.  

# io.micrometer
https://www.codeprimers.com/metrics-collection-in-spring-boot-with-micrometer-and-prometheus/