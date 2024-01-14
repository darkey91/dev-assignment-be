create table airport
(
    id      serial primary key,
    name    varchar(255) unique not null,
    code    varchar(20) unique  not null,
    country varchar(60)         not null
);

create table flight
(
    id                serial primary key,
    code              varchar(20) unique not null,
    orig_airport_code varchar(20)        not null,
    dest_airport_code varchar(20)        not null,

    departure_time    timestamp          not null,
    arrival_time      timestamp          not null,

    passenger_count   smallint           not null default 0,

    foreign key (orig_airport_code)
        references airport (code),

    foreign key (dest_airport_code)
        references airport (code)
);

create table passenger
(
    id          serial primary key,
    name        varchar(1024) not null,
    flight_code varchar(20)   not null,

    foreign key (flight_code)
        references flight (code)
);

create unique index passenger__name_fl_code_idx on passenger (name, flight_code);

create index flight__orig_airport_code_depart_time_idx on flight (orig_airport_code, departure_time);