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
  dagr_name                     varchar(255),
  author                        varchar(255),
  document_name                 varchar(255),
  content_type                  varchar(255),
  resource_location             varchar(255),
  file_size                     bigint,
  dagr_uuid                     varchar(40),
  creation_date                 datetime(6) not null,
  document_creation_date        datetime(6) not null,
  last_modified                 datetime(6) not null,
  constraint pk_dagr primary key (id)
);

create table dagr_dagr (
  dagr_id                       bigint auto_increment not null,
  constraint pk_dagr_dagr primary key (dagr_id)
);

create table dagr_annotation (
  dagr_id                       bigint not null,
  annotation_id                 bigint not null,
  constraint pk_dagr_annotation primary key (dagr_id,annotation_id)
);

alter table dagr_dagr add constraint fk_dagr_dagr_dagr_1 foreign key (dagr_id) references dagr (id) on delete restrict on update restrict;
create index ix_dagr_dagr_dagr_1 on dagr_dagr (dagr_id);

alter table dagr_dagr add constraint fk_dagr_dagr_dagr_2 foreign key (dagr_id) references dagr (id) on delete restrict on update restrict;
create index ix_dagr_dagr_dagr_2 on dagr_dagr (dagr_id);

alter table dagr_annotation add constraint fk_dagr_annotation_dagr foreign key (dagr_id) references dagr (id) on delete restrict on update restrict;
create index ix_dagr_annotation_dagr on dagr_annotation (dagr_id);

alter table dagr_annotation add constraint fk_dagr_annotation_annotation foreign key (annotation_id) references annotation (id) on delete restrict on update restrict;
create index ix_dagr_annotation_annotation on dagr_annotation (annotation_id);


# --- !Downs

alter table dagr_dagr drop foreign key fk_dagr_dagr_dagr_1;
drop index ix_dagr_dagr_dagr_1 on dagr_dagr;

alter table dagr_dagr drop foreign key fk_dagr_dagr_dagr_2;
drop index ix_dagr_dagr_dagr_2 on dagr_dagr;

alter table dagr_annotation drop foreign key fk_dagr_annotation_dagr;
drop index ix_dagr_annotation_dagr on dagr_annotation;

alter table dagr_annotation drop foreign key fk_dagr_annotation_annotation;
drop index ix_dagr_annotation_annotation on dagr_annotation;

drop table if exists annotation;

drop table if exists dagr;

drop table if exists dagr_dagr;

drop table if exists dagr_annotation;

