drop table if exists comment;
drop table if exists news;

create table if not exists news
(
    id    bigserial primary key,
    time  TIMESTAMP    not null,
    title varchar(255) not null,
    text  text         not null
);

create table if not exists comment
(
    id       bigserial primary key,
    news_id  BIGINT,
    time     TIMESTAMP    not null,
    text     varchar(255) not null,
    username varchar(40)  not null,
    CONSTRAINT fk_news FOREIGN key (news_id) references news (id) on delete cascade
);
