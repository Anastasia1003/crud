create procedure delete_query(IN t_name varchar(20), IN p_name varchar(20), IN p_value varchar(100))
BEGIN
        SET @dyn_sql = CONCAT('delete from ', t_name, ' where ', p_name, ' = \'', p_value, '\'');
    PREPARE s1 from @dyn_sql;
    EXECUTE s1;
    DEALLOCATE PREPARE s1;
END;