# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table annotation (
  id                            bigint auto_increment not null,
  annotation_text               varchar(255),
  creation_date                 datetime(6) not null,
  constraint pk_annotation primary key (id)
);

create table dagr (
  id                            bigint auto_increment not null,
  dagr_uuid                     varchar(40),
  dagr_name                     varchar(255),
  author                        varchar(255),
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

create table dagr_component_annotation (
  dagr_component_id             bigint not null,
  annotation_id                 bigint not null,
  constraint pk_dagr_component_annotation primary key (dagr_component_id,annotation_id)
);

alter table dagr_component add constraint fk_dagr_component_dagruuid foreign key (dagruuid) references dagr (id) on delete restrict on update restrict;
create index ix_dagr_component_dagruuid on dagr_component (dagruuid);

alter table dagr_component_annotation add constraint fk_dagr_component_annotation_dagr_component foreign key (dagr_component_id) references dagr_component (id) on delete restrict on update restrict;
create index ix_dagr_component_annotation_dagr_component on dagr_component_annotation (dagr_component_id);

alter table dagr_component_annotation add constraint fk_dagr_component_annotation_annotation foreign key (annotation_id) references annotation (id) on delete restrict on update restrict;
create index ix_dagr_component_annotation_annotation on dagr_component_annotation (annotation_id);


# --- !Downs

alter table dagr_component drop foreign key fk_dagr_component_dagruuid;
drop index ix_dagr_component_dagruuid on dagr_component;

alter table dagr_component_annotation drop foreign key fk_dagr_component_annotation_dagr_component;
drop index ix_dagr_component_annotation_dagr_component on dagr_component_annotation;

alter table dagr_component_annotation drop foreign key fk_dagr_component_annotation_annotation;
drop index ix_dagr_component_annotation_annotation on dagr_component_annotation;

drop table if exists annotation;

drop table if exists dagr;

drop table if exists dagr_component;

drop table if exists dagr_component_annotation;

