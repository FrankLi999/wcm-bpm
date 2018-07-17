create table usersc (
  id integer primary key,
  first_name varchar(255),
  last_name varchar(255)
);
create sequence hibernate_sequence start with 1 increment by 1;
create sequence usersc_sequence start with 1 increment by 1;
create table users (
  id integer primary key,
  username varchar(255) UNIQUE,
  password varchar(255),
  email varchar(255) UNIQUE,
  bio text,
  image varchar(511)
);

create table articles (
  id integer primary key,
  user_id integer,
  slug varchar(255) UNIQUE,
  title varchar(255),
  description text,
  body text,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

create table article_favorites (
  article_id integer not null,
  user_id integer not null,
  primary key(article_id, user_id)
);

create table follows (
  user_id integer not null,
  target_id integer not null,
  primary key(user_id, target_id)
);

create table tags (
  id integer primary key,
  name varchar(255)
);

create table article_tags (
  article_id integer not null,
  tag_id integer not null
);

create table comments (
  id integer primary key,
  body text,
  article_id integer,
  user_id integer,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);