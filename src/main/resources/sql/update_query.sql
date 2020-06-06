create procedure update_query(IN t_name varchar(20), IN p1_name varchar(20), IN p1_value varchar(100), IN p2_name varchar(20), IN p2_value varchar(100))
BEGIN
        SET @dyn_sql = CONCAT('update ', t_name, ' set ', p1_name, ' = \'', p1_value, '\' where ', p2_name, ' = \'', p2_value, '\'');
    PREPARE s1 from @dyn_sql;
    EXECUTE s1;
    DEALLOCATE PREPARE s1;
END;