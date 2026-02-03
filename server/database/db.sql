CREATE TYPE user_status AS ENUM ('pending', 'active', 'inactive', 'suspended');
create table users
(
    id       uuid         not null
        constraint user_pk
            primary key,
    name     varchar(255) not null unique ,
    email    varchar(255) not null unique,
    password varchar(255) not null,
    image    varchar(255),
    status     user_status DEFAULT 'pending'
);
