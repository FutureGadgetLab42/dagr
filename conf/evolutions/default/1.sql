# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table annotation (
  id                            bigint auto_increment not null,
  annotation                    varchar(255),
  creation_date                 datetime(6) not null,
  constraint pk_annotation primary key (id)
);

create table dagr (
  id                            bigint auto_increment not null,
  dagr_uuid                     varchar(40),
  creation_date                 datetime(6) not null,
  constraint pk_dagr primary key (id)
);

create table dagr_component (
  id                            bigint auto_increment not null,
  last_modified                 datetime(6),
  dagr_component_uuid           varchar(40),
  dagruuid                      bigint not null,
  resource_location             varchar(255),
  dagr_component_creation_date  datetime(6) not null,
  constraint pk_dagr_component primary key (id)
);

alter table dagr_component add constraint fk_dagr_component_dagruuid foreign key (dagruuid) references dagr (id) on delete restrict on update restrict;
create index ix_dagr_component_dagruuid on dagr_component (dagruuid);


# --- !Downs

alter table dagr_component drop foreign key fk_dagr_component_dagruuid;
drop index ix_dagr_component_dagruuid on dagr_component;

drop table if exists annotation;

drop table if exists dagr;

drop table if exists dagr_component;

