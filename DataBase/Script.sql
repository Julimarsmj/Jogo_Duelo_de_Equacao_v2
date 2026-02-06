create database dbjogador;

use dbjogador;

create table tbjogador(
id int primary key auto_increment,
nomejogador varchar(100) not null,
idade int not null,
escolaridade varchar(50),
niveljogo varchar(15),
avatar longblob,
pontos int default 0
);

desc tbjogador;