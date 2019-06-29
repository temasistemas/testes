create table if not exists Contato (
  Id int auto_increment primary key not null,
  Nome varchar(100) not null,
  Email varchar(200) null,
  telefone varchar(20) null
);
