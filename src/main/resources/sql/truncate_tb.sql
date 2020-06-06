CREATE PROCEDURE truncate_tb(In tableName varchar(25))
BEGIN
        SET @truncatedb = CONCAT('TRUNCATE TABLE ', tableName );
    PREPARE truncate FROM @truncatedb ;
    EXECUTE truncate ;
    DEALLOCATE PREPARE truncate ;
END;