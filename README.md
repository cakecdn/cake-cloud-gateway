# Cake Cloud Gateway

CakeCDN gateway module powered by [Spring Cloud Netflix Zuul](https://github.com/Netflix/zuul).

## Introduction

This is the main RESTful API endpoint of the whole CakeCDN.

**WARNING: Do Not Implement This Repository Directly Into Your Project Without
 Any Adaptive Modification of The Codes.**

## Environmental Dependencies

Following are suggested environmental dependencies, higher versions are allowed.

**Development**

* JDK 1.8

* Maven 3.6.0

* MySQL 5.6

**Production**

* JRE 1.8

* MariaDB 10.1.3

## Database Config

It is suggested that you should configure your database through system environment
 variable rather than write it directly in config file like `application.yml`, as
 the host, username and password of your development environment are all in public
 when you push your codes into GitHub.

Followings are required environment variables and their default values:

> CAKE_DB_HOST=localhost
>
> CAKE_DB_PORT=3306
>
> CAKE_DB_USERNAME=cake_db_sample_username
>
> CAKE_DB_PASSWORD=cake_db_sample_password
>
> CAKE_DB_DATABASE=cake_db_sample_database

Don't forget to config them if you deploying the project in docker container.

## JWT Authorization

As a replacement for traditional Session-Cookies method, JWT(JSON Web Token) is a
stateless token authentication that stores the login status in client side.

[More About JWT](https://jwt.io)

The ajax requests from clients must bring its `Authorization` HTTP request header
 contains the JWT token when login.

 > Authorization: Bearer eyJhbGciOiJI...

Default prefix `Bearer` could be configured in `application.yml` or `bootstrap.yml`
 of any active environment profile.


## License

[Apache License 2.0](LICENSE)