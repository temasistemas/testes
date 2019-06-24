
Compilar projeto

```
mvn clean install
```

Compilar com o sonar
```
mvn clean install jacoco:prepare-agent jacoco:report sonar:sonar -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=seutokenaqui
```
