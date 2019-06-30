# Workshop Testes unitários na Temasistemas

Código de exemplo no repositorios de [testes](https://github.com/temasistemas/testes)

Vídeos do Workshop 29/06/2019

https://github.com/temasistemas/testes/videos

# Compilar projeto

```
mvn clean install
```

# Compilar com o sonar
```
mvn clean install jacoco:prepare-agent jacoco:report sonar:sonar -Dsonar.projectKey=temasistemas_testes -Dsonar.organization=temasistemas-github -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=tokenaqui
```
