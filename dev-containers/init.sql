create table airports (
    id varchar (64) primary key,
    name varchar(128) unique not null,
    code varchar(32) unique not null,
    country varchar(128) not null
);

create table flights
(
    id varchar (64) primary key,
    code varchar(32) unique not null,
    orig_airport_id varchar (64) not null,
    dest_airport_id varchar (64) not null,

    departure_time timestamp not null,
    arrival_time smallint not null default 0,

    passenger_count  not null,

    foreign key (orig_airport_id)
        references airports (id),

    foreign key (dest_airport_id)
        references airports (id)
);

create table passengers(
    id varchar (64) primary key,

    name varchar (128) not null,

    flight_code varchar(32) not null,

    foreign key (flight_code)
        references flights (code)
);

create unique index passenger__name_fl_code_idx on passengers (name, flight_code);

create index flight__orig_airport_id_depart_time_idx on flights (orig_airport_id, departure_time);