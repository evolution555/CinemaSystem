# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table film (
  title                         varchar(255) not null,
  director                      varchar(255),
  trailer_url                   varchar(255),
  duration                      integer,
  summery                       varchar(255),
  constraint pk_film primary key (title)
);

create table user (
  email                         varchar(255) not null,
  name                          varchar(255),
  role                          varchar(255),
  password                      varchar(255),
  constraint pk_user primary key (email)
);


# --- !Downs

drop table if exists film;

drop table if exists user;

