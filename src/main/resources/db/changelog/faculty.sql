--liquibase formatted sql

--changeset Iurchenko:2

create index faculty_name_and_color_index on faculty(name, color)