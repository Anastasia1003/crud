CREATE PROCEDURE insert_tb(IN name varchar(50), IN author varchar(50))
BEGIN
    insert into Book (name, author) values (name, author);
    SELECT LAST_INSERT_ID();
END;