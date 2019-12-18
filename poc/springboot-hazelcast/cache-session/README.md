# Hazelcast in Spring-Boot

This sample app demonstrate how to use Hazelcast in a Spring-Boot app. This app shows

 - Hazelcast as Spring-Cache implementation for method caching
 - Hazelcast as Hibernate 2nd Level Cache
 - Hazelcast as Spring-Session Cache
 
 
## Usage

Compile the app: `mvn clean package`

### Running the app

You can configure Hazelcast as embedded to your application or as a client to connect to a remote Hazelcast
cluster


#### Running as Embedded

Run: `mvn spring-boot:run -Dspring.profiles.active=hz-embedded`


#### Running as Client

You need to Have a running cluster with user code deployment enabled. You can use 
[HzMember](src/test/java/com/oner/demo/seajug/hazelcast/scalewithhazelcast/HzMember.java) class to start a Hazelcast
 member (You can run the ame  & create 2 node Hazelcast cluster)

Then run: `mvn spring-boot:run -Dspring.profiles.active=hz-client`

### Testing

Spring-Boot app uses Hazelcast 3.10.4. You can download latest Hazelcast Management Center from 
[here](https://download.hazelcast.com/management-center/hazelcast-management-center-3.10.2.zip) & start it to 
monitor the cluster.

App starts the Tomcat on a random port. Check the logs & find the port number.

#### Method Caching

Visit `http://localhost:{port}/rest/fib/{number}` URL (replace `port` with Tomcat port and `number` with a 
random number). It'll calculate & cache the `fin(number)`, print result & time spent. Next, call the same url & see
result comes from cache directly.

In the Hazelcast Management Center, you can monitor the Map called `fib` as well.

### Hibernate 2nd Level Cache

Visit `http://localhost:{port}/db/{id}` URL (replace `port` with Tomcat port and `id` with a random number).
There is records for ids 1 to 5. If you enter an `id` greater than 5, it'll create a new record, store it
in the embedded H2 db using Hibernate & it'll be cached in Hazelcast.

In the Hazelcast Management Center, you can monitor the Map called 
`com.oner.demo.seajug.hazelcast.scalewithhazelcast.model.MyPojo` as well.

### Spring session caching

Visit `http://localhost:{port}/secure/salute` URL (replace `port` with Tomcat port). Use `admin` as username
and password.

In the Hazelcast Management Center, you can see a the Map called `spring:session:sessions` created and
 contains 1 record (session).
 
You can start the same app again, another instance, and visit the same URL from same browser just 
chaning the port. You'll see that session is shared between 2 instances of same app & you don't need to enter
credentials again.