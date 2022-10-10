use kata_db;

drop table if exists users_roles;
drop table if exists roles;
drop table if exists users;

create table users
(
    id       bigint      not null auto_increment,
    username varchar(50) not null,
    password varchar(255),
    name     varchar(20) not null,
    lastname varchar(20),
    age      tinyint,
    primary key (id)
);

create table roles
(
    id   bigint      not null auto_increment,
    name varchar(20) not null,
    primary key (id)
);

create table users_roles
(
    user_id bigint not null,
    role_id bigint not null,
    primary key (user_id, role_id),
    foreign key (user_id) references users (id),
    foreign key (role_id) references roles (id)
);

select *
from roles;