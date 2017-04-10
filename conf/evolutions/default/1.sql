# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table booking (
  booking_id                    integer not null,
  title                         varchar(255),
  time                          varchar(255),
  date                          varchar(255),
  qty                           integer,
  total                         double,
  cost                          double,
  constraint pk_booking primary key (booking_id)
);
create sequence booking_seq;

create table film (
  title                         varchar(255) not null,
  director                      varchar(255),
  trailer_url                   varchar(255),
  duration                      integer,
  summery                       varchar(255),
  constraint pk_film primary key (title)
);

create table messages (
  id                            varchar(255) not null,
  name                          varchar(255),
  email                         varchar(255),
  message                       varchar(255),
  constraint pk_messages primary key (id)
);

create table payments (
  card_number                   varchar(255) not null,
  name                          varchar(255),
  exp_month                     varchar(255),
  exp_year                      integer,
  cvv2                          integer,
  constraint pk_payments primary key (card_number)
);

create table showing (
  showing_id                    varchar(255) not null,
  title                         varchar(255),
  screen                        integer,
  date                          varchar(255),
  constraint pk_showing primary key (showing_id)
);

create table showing_time (
  id                            varchar(255) not null,
  time                          varchar(255),
  showingid                     varchar(255),
  constraint pk_showing_time primary key (id)
);
create sequence showing_time_seq increment by 1;

create table staff (
  id                            bigint not null,
  name                          varchar(255),
  job_role                      varchar(255),
  description                   varchar(255),
  email                         varchar(255),
  number                        varchar(255),
  constraint pk_staff primary key (id)
);
create sequence staff_seq;

create table user (
  email                         varchar(255) not null,
  name                          varchar(255),
  role                          varchar(255),
  password                      varchar(255),
  constraint pk_user primary key (email)
);

create table carousel (
  title                         varchar(255) not null,
  description                   varchar(255),
  constraint pk_carousel primary key (title)
);

alter table showing_time add constraint fk_showing_time_showingid foreign key (showingid) references showing (showing_id) on delete restrict on update restrict;
create index ix_showing_time_showingid on showing_time (showingid);


# --- !Downs

alter table showing_time drop constraint if exists fk_showing_time_showingid;
drop index if exists ix_showing_time_showingid;

drop table if exists booking;
drop sequence if exists booking_seq;

drop table if exists film;

drop table if exists messages;

drop table if exists payments;

drop table if exists showing;

drop table if exists showing_time;
drop sequence if exists showing_time_seq;

drop table if exists staff;
drop sequence if exists staff_seq;

drop table if exists user;

drop table if exists carousel;

