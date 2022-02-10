

# Project for various tests
Stack 
<li>Angular 11</li>
<li>SpringBoot</li>
<li>PostgreSQL</li>

You can check how it behaves on my [Heroku](https://www.heroku.com/platform) server  here:

[**Online-Med-Site**](https://online-med-ui.herokuapp.com/)


Server requires around ~30s to start.(You will see correct translations)

Test user:

```
Login   : admin
Password: admin123
```
One disclaimer if you would like to configure sending mails you should set 
`spring.mail.username` and `spring.mail.password` in ``onlinemed-master/src/main/resources/application.prod.properties``
it is usually your login and password for your gmail account.



###Custom profile
/usr/lib/jvm/jdk-15/bin/java -jar --enable-preview
{path-to-jar}/onlinemed/target/onlinemed-1.0.0.jar
--spring.config.location=classpath:/application.prod.properties 

### JVM command used to check NullPointerException
```-XX:+ShowCodeDetailsInExceptionMessages```