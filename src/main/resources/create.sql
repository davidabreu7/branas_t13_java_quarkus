-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(1, 'field-1');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;

create schema cccat13;

create table cccat13.account (
                                 account_id uuid,
                                 name text,
                                 email text,
                                 cpf text,
                                 car_plate text,
                                 is_passenger boolean,
                                 is_driver boolean,
                                 date timestamp,
                                 is_verified boolean,
                                 verification_code uuid
);

create table cccat13.ride (
                              ride_id uuid,
                              passenger_id uuid,
                              driver_id uuid,
                              status text,
                              fare numeric,
                              distance numeric,
                              from_lat numeric,
                              from_long numeric,
                              to_lat numeric,
                              to_long numeric,
                              date timestamp
);

create table cccat13.position (
                                  position_id uuid primary key,
                                  ride_id uuid,
                                  lat numeric,
                                  long numeric,
                                  date timestamp
);