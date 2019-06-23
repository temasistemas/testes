create table if not exists FonteDados (
  NomFonte varchar(100) primary key not null,
  JdbcUrl varchar(200) not null,
  Usuario varchar(200) not null,
  Senha varchar(200) not null
);