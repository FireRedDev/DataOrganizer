drop table dateiendung;
drop table regexrules;

create table dateiendung (
    datatype varchar(100) not null,
    extension varchar(132) not null
);
create table regexrules (
    ordner varchar(100) not null,
    regex varchar(132) not null
);