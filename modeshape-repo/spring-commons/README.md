# Spring Commons

> Minimize your development cost with our intensive consulting/training — [click here](https://www.bpwizard.com/) for details.

When developing **real-world** Spring REST APIs and microservices, you face many challenges like

1. How to follow a stateless and efficient security model – using JWT authentication, session sliding etc.
1. How to configure Spring Security to suit API development, e.g. returning _200_ or _401_ responses on login, configuring _CORS_, _JSON vulnerability protection_, etc.
1. How to elegantly do _validations_ and _exceptions_ and send precise errors to the client.
1. How to easily mix manual and bean validations in a single validation cycle.
1. How exactly to support multiple _social sign up/in_, using _OpenID Connect_ or _OAuth2_ providers such as _Google_ and _Facebook_.
1. How to code a robust user module (with features like _sign up_, _sign in_, _verify email_, _social sign up/in_, _update profile_, _forgot password_, _change password_, _change email_, _token authentication_ etc.) and share it across all your applications.
1. How to correctly and effeciently _secure microservices_, using long-lived and short-lived JWTs.
1. What would be good ways to _test your API_.
1. How to do _Captcha validation_.
1. How to properly organize _application properties_.
1. How to use _PATCH_ and _JsonPatch_ to handle partial updates correctly.
1. How to do all the above reactively, using WebFlux and WebFlux security.

Coding all this rightly needs in-depth knowledge of Spring. It also takes a lot of development time and effort, and needs to be properly maintained as new versions of Spring comes out.

**Spring Commons** relieves you of all this burden. It's a set of configurable and extensible libraries, providing all above features. Use these to develop quality reactive or non-reactive monolith or microservices applications quickly and easily.  

Even if you don't plan to use Spring Commons, it's a good example to learn from, because it showcases the essential best practices for developing elegant web services and microservices using Spring.

Most Spring Boot applications can use Spring Commons straight away, with some simple configurations. But, if you don't find it suitable for your application, feel free to fork it, or just roll out your own library by learning its patterns and practices. Better yet, be a contributor!

Read [this quick starter guide](https://github.com/bpwizard/spring-commons/wiki/Getting-Started-With-Spring-Commons) or watch [this video tutorial](https://www.bpwizard.com/p/spring-commons-restful-web-services-development) for getting started.

## Libraries hierarchy

* [spring-commons-exceptions](https://github.com/bpwizard/spring-commons/wiki/Spring-Commons-Exceptions-Guide): Useful for elegant exception handling and validation in any Spring project
    * [spring-commons-commons](https://github.com/bpwizard/spring-commons/wiki/Spring-Commons-Commons-Guide): Common for all things below
        * [spring-commons-commons-web](https://github.com/bpwizard/spring-commons/wiki/Spring-Commons-Commons-Web-Guide): For developing Spring Web (non-reactive) microservices
            * [spring-commons-commons-jpa](https://github.com/bpwizard/spring-commons/wiki/Spring-Commons-Commons-JPA-Guide): For developing Spring Web (non-reactive) JPA microservices
                * [spring-commons-jpa](https://github.com/bpwizard/spring-commons/wiki/Spring-Commons-JPA-Guide): For developing Spring Web (non-reactive) JPA monolith or auth-microservice
        * [spring-commons-commons-reactive](https://github.com/bpwizard/spring-commons/wiki/Spring-Commons-Commons-Reactive-Guide): For developing Spring WebFlux (reactive) microservices
            * [spring-commons-commons-mongo](https://github.com/bpwizard/spring-commons/wiki/Spring-Commons-Commons-MongoDB-Guide): For developing Spring WebFlux (reactive) MongoDB microservices
                * [spring-commons-reactive](https://github.com/bpwizard/spring-commons/wiki/Spring-Commons-Reactive-Guide): For developing Spring WebFlux (reactive) MongoDB monolith or auth-microservice

For example usages, see

* [Demo non-reactive monolith](https://github.com/bpwizard/spring-commons/tree/master/commons-demo-jpa)
* [Demo reactive monolith](https://github.com/bpwizard/spring-commons/tree/master/commons-demo-reactive)
* [Demo non-reactive microservices](https://github.com/bpwizard/np-microservices-sample-01) and its [configuration repository](https://github.com/bpwizard/np-microservices-sample-01-config)
* [Demo reactive microservices](https://github.com/bpwizard/np-microservices-sample-02) and its [configuration repository](https://github.com/bpwizard/np-microservices-sample-02-config)

## Documentation and Resources

> _Our [Spring Framework Recipes For Real World Application Development](https://www.bpwizard.com/p/spring-framework-book-of-best-practices) — a live book discussing key real-world topics on developing Spring applications and APIs — is now available for FREE. [Click here](https://www.bpwizard.com/p/spring-framework-book-of-best-practices) to get it._

1. Feature demo: https://youtu.be/6mNg-Feq8CY
1. _Getting started guide_
   1. [Book](https://github.com/bpwizard/spring-commons/wiki/Getting-Started-With-Spring-Commons)
   1. [Video Tutorial](https://www.bpwizard.com/p/spring-commons-restful-web-services-development)
1. _[Official Documentation](https://github.com/bpwizard/spring-commons/wiki)_
1. _Example applications_
    * [Demo non-reactive monolith](https://github.com/bpwizard/spring-commons/tree/master/commons-demo-jpa)
    * [Demo reactive monolith](https://github.com/bpwizard/spring-commons/tree/master/commons-demo-reactive)
    * [Demo non-reactive microservices](https://github.com/bpwizard/np-microservices-sample-01) and its [configuration repository](https://github.com/bpwizard/np-microservices-sample-01-config)
    * [Demo reactive microservices](https://github.com/bpwizard/np-microservices-sample-02) and its [configuration repository](https://github.com/bpwizard/np-microservices-sample-02-config)
1. _[API documentation](https://documenter.getpostman.com/view/305915/RVu2mqEH)_ of the above applications.
1. _[Example AngularJS front-end application](https://github.com/bpwizard/spring-commons/tree/master/commons-demo-angularjs)_ — A sample AngularJS 1.x front-end. It'll work for the application developed in the above [getting started guide](https://github.com/bpwizard/spring-commons/wiki/Getting-Started-With-Spring-Commons) as well all the above example applications. See the [Getting Started Guide](https://github.com/bpwizard/spring-commons/wiki/Getting-Started-With-Spring-Commons) on how to use it.
1. _[Spring Framework Recipes For Real World Application Development](https://www.bpwizard.com/p/spring-framework-book-of-best-practices)_ — a live book discussing key real-world topics on developing Spring applications, APIs and microservoces. Includes many Spring Commons topics. [Click here](https://www.bpwizard.com/p/spring-framework-book-of-best-practices) to get it now for FREE!
1. [Using Spring Commons Effectively](https://github.com/bpwizard/spring-commons/wiki/Using-Spring-Commons-Effectively)
1. [DZone Articles](https://dzone.com/users/1211183/skpatel20.html)
1. Video tutorials coming soon:
   1. Spring Framework 5 REST API Development — A Complete Blueprint For Real-World Developers
   1. Spring WebFlux Reactive REST API Development — A Complete Blueprint For Real-World Developers
   1. Microservices Using Spring Cloud — A Rapid Course For Real World Developers
   1. Join [here](https://www.bpwizard.com/p/spring-framework-book-of-best-practices) to get notified and avail heavy discounts when the above courses get released

## Help and Support
1. Community help is available at [stackoverflow.com](http://stackoverflow.com/questions/tagged/spring-commons), under the `spring-commons` tag. Do not miss to tag the questions with `spring-commons`!
1. [Submit an issue](https://github.com/bpwizard/spring-commons/issues) for any bug or enhancement. Please check first that the issue isn't already reported earlier.
1. Mentoring, training and professional help is provided by [bpwizard.com](https://www.bpwizard.com).

## Releases and Breaking Changes

1. See [here](https://github.com/bpwizard/spring-commons/releases).
