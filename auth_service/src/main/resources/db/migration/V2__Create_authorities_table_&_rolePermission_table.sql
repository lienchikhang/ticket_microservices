
create table Authorities (
     authority_id int primary key not null auto_increment,
     authority_name varchar(20) not null unique,
     is_active boolean default true,
     created_at datetime,
     update_at datetime
);

create table Role_Permission (
     role_id int,
     authority_id int,

     primary key (role_id, authority_id),
     foreign key (role_id) references Roles(role_id),
     foreign key (authority_id) references Authorities(authority_id)
);