## Beyond TaskManager homework

The description for the task can be found here:
**[https://bitbucket.org/one-way/blackswan-tests/src/master/bs-mid-to-senior-java-dev.md](https://bitbucket.org/one-way/blackswan-tests/src/master/bs-mid-to-senior-java-dev.md)**.



### Requirements to run and test the application:

- JDK 11
- Maven (clean install)
- Docker (for prod profile)
### Tests
**Jacoco** was injected for code coverage measurement. Jacoco generates report to  ***target/jacoco/index.html*** file.

### Database migration and dockerization
**Liquibase** was used for database schema management.
The application has 3 profiles : **dev**, **local**, and **prod**.

Dockerfile is provided in the project directory, its starts the application with prod profile. The Dockerfile also runs the Liquibase migration scripts. The prerequsitie for this was to run the application in local environment with enabled generation properties.  So it will create the testLocalDB which the Dockerfile uses.
> spring.datasource.url=jdbc:h2:~/testLocalDB
> spring.jpa.generate-ddl=true
spring.jpa.show-sql=true
spring.jpa.open-in-view=true
spring.jpa.properties.eclipselink.weaving=true
spring.jpa.hibernate.ddl-auto=update
spring.liquibase.enabled=true


## Bonus task

There is ***ScheduledJob*** class in application.task package which is responsible to update all *PENDING* Tasks which are "expired"  to *DONE* status.
In the current setup it runs every minute (was convenient during development and testing) but it can be changed in the properties file.
>1 minute 
> 
>schedulerjob.thread.frequency=60000


## Further Development

Obviously there is a lot of space for it, here are just a few:

- PROCESSING_MESSAGE column in BEYOND_TASK table which would have information about the phase or problem if occurs during task processing. (for debugging)
- extend processing statuses with IDLE for example
- TASK_QUANTITY column in BEYOND_USER table , just as a "nice to have".
- Background job which deletes the tasks which are done and no need to keep it in the database.
- Login and authentication for users
- Service to handle inactive users
