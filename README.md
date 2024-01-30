

# Project for various tests
Stack 
<li>Angular 11</li>
<li>SpringBoot</li>
<li>Jdk 17</li>
<li>Prometheus</li>
<li>Grafana</li>
<li>Nginx</li>
<li>Docker</li>

### Build project via maven command:
``mvn clean package`` and than ``docker compose up``

Test user:

```
Login   : admin
Password: admin123
```
One disclaimer if you would like to configure sending mails you should set 
`spring.mail.username` and `spring.mail.password` in ``onlinemed-master/src/main/resources/application.prod.properties``
it is usually your login and password for your gmail account.

# Some screens

<img align="left" title="ss1" alt="ss1" width="1200px" src="https://github.com/albi23/onlinemed/blob/master/docs/drugs.png" />
<br>
<img align="left" title="ss2" alt="ss2" width="1200px" src="https://github.com/albi23/onlinemed/blob/master/docs/forum.png" />
<br>
<img align="left" title="ss3" alt="ss3" width="1200px" src="https://github.com/albi23/onlinemed/blob/master/docs/login.png" />
<br>
<img align="left" title="ss4" alt="ss4" width="1200px" src="https://github.com/albi23/onlinemed/blob/master/docs/not.png" />
<br>
<img align="left" title="ss5" alt="ss5" width="1200px" src="https://github.com/albi23/onlinemed/blob/master/docs/profile.png" />
<br>
<img align="left" title="ss6" alt="ss6" width="1200px" src="https://github.com/albi23/onlinemed/blob/master/docs/topic.png" />
<br>
<img align="left" title="ss7" alt="ss7" width="1200px" src="https://github.com/albi23/onlinemed/blob/master/docs/usr_mgm.png" />
<br>
<br>
<br>




### Custom profile
/usr/lib/jvm/jdk-15/bin/java -jar --enable-preview
{path-to-jar}/onlinemed/target/onlinemed-1.0.0.jar
--spring.config.location=classpath:/application.prod.properties 

### JVM command used to check NullPointerException
```-XX:+ShowCodeDetailsInExceptionMessages```

### JVM run args

```--add-opens java.base/jdk.internal.reflect=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED```

docker compose build <service>