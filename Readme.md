# Workshop Testes unitários na Temasistemas

1. O que é um teste unitário?
2. Primeiro teste com JUnit (@Test, Assert)
3. Usando o PowerMock e Mockito
4. Como fazer um @Mock
5. Como treinar um Mock (When, doNothing, Awnser)
6. Como fazer um @Captor
7. Como fazer um Mock de métodos estáticos (@PowerMockIgnore)
8. O que é um database H2
9. DBUnit como usar
10. Teste com DBUnit e H2
11. Família MocksFor para teste
12. Qualidade de código com SONAR
13. Verificando cobertura de teste com Jacoco

Código de exemplo no repositorios de [testes](https://github.com/temasistemas/testes)

[Vídeos](https://github.com/temasistemas/testes/tree/master/videos) do Workshop 29/06/2019

https://github.com/temasistemas/testes/tree/master/videos

[Apresentação](https://github.com/temasistemas/testes/blob/master/Workshop%20Testes%20Unit%C3%A1rios.pptx)

# Compilar projeto

```
mvn clean install
```

# Compilar com o sonar
```
mvn clean install jacoco:prepare-agent jacoco:report sonar:sonar -Dsonar.projectKey=temasistemas_testes -Dsonar.organization=temasistemas-github -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=tokenaqui
```
