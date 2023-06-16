insert into news(id, time, title, text)
values (1,
        '2023-05-18 15:34:42.709394',
        'One-Stop Guide to Database Migration with Liquibase and Spring Boot',
        'This guide provides an overview of Liquibase and how to use it in a Spring Boot application for managing and applying database schema changes.'),
       (2,
        '2023-05-18 15:34:43.709394',
        'Ghibli Museum releases beautiful new line of anime art T-shirts, available for online orders',
        'Shirts take inspiration from memorable sights at Tokyo’s Ghibli Museum, offered in both adult and kid sizes. Available in white or dark green, the shirt’s back illustration depicts a clockwork art installation found within the Ghibli Museum in Tokyo.');

select setval('news_id_seq', (select(max(id)) from news));
