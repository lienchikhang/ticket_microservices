create table Users (
  id int primary key auto_increment,
  email varchar(250) not null unique,
  phone varchar(11) not null unique,
  password varchar(255) not null,
  first_name varchar(255),
  last_name varchar(255),
  created_at timestamp default now(),
  updated_at timestamp default now(),
  is_active boolean default true,
  gender bit,
  role_id int not null,

  foreign key (role_id) references Roles(role_id)
);

-- #This bellow table is use for store userId and publicKey
create table KeyTokens (
    user_id int not null primary key,
    public_key text not null,
    refresh_token text
);