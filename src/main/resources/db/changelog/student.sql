--liquibase formatted sql

--changeset Iurchenko:1

create index student_name_index on student(name)