create procedure select_all_tb(In tableName varchar(25))
begin
        SET @selectdb = CONCAT('SELECT * FROM ', tableName );
    PREPARE select_tb FROM @selectdb ;
    EXECUTE select_tb ;
    DEALLOCATE PREPARE select_tb ;
END;