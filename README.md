# blog-microservices-v1 [![Quality Gate Status](http://ec2-54-169-150-26.ap-southeast-1.compute.amazonaws.com:9000/api/project_badges/measure?project=Blog-Microservices-Ehancement&metric=alert_status)](http://ec2-54-169-150-26.ap-southeast-1.compute.amazonaws.com:9000/dashboard?id=Blog-Microservices-Ehancement)
This microservices project used Swagger-ui to manage API list. 
Json web token used to handle authentication process in gateway application and will also be used in all microservices using 
secret key sharing between gateway application and its microservices. 

## Architecture

<img width="516" alt="Microservice Architecture" src="https://user-images.githubusercontent.com/18225438/70013213-8ac45380-15a9-11ea-9786-e42bc4bfacbb.PNG">

## The Stacks:
1. Springboot 2.1.6
2. MySQL
3. Swagger-ui
4. MockMVC for Integration testing
5. Cucumber & JUnit for High Level's Like Integration Testing
6. Cucumber for Frontend High Level Testing (Frontend: https://github.com/syarbeats/blog-frontend-application.git)
7. Chrome Webdriver


### Postman Screenshot Example (Get All Posts for certain category)

Gateway: /services/blog

Microservices: /api/posts/category

<img width="740" alt="GetBlog" src="https://user-images.githubusercontent.com/18225438/64406820-fa12d000-d0ac-11e9-940e-4f5c8ad52d7a.PNG">

### Swagger-ui Screenshoot

<img width="355" alt="swager blog" src="https://user-images.githubusercontent.com/18225438/64321511-ddae5f00-cfea-11e9-8dd6-0aaf178c981a.PNG">



