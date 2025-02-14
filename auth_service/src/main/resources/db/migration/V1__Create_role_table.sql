create table Role
(
    role_id   int primary key not null auto_increment,
    role_name varchar(20)     not null unique,
    is_active boolean default true
)