CREATE PROCEDURE create_tb(In tableName varchar(25))
BEGIN
    SET @sql = CONCAT('CREATE TABLE IF NOT EXISTS ', tableName);
        SET @sql = CONCAT(@sql, '(
	id int auto_increment,
	name varchar(50) null,
	author varchar(50) null,
	constraint table_name_pk
		primary key (id)
)');
    PREPARE s FROM @sql;
    EXECUTE s;
    DEALLOCATE PREPARE s;
END;