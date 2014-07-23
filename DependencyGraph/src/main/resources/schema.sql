--DROP TABLE obj_dependency;

CREATE TABLE obj_dependency( 
	id INT NOT NULL, 
	installed BOOLEAN NOT NULL DEFAULT FALSE, 
	installed_explicit BOOLEAN NOT NULL DEFAULT FALSE, 
	obj VARCHAR(64) NOT NULL, 
	dependent_on VARCHAR(64) NOT NULL, 
	PRIMARY KEY(obj, dependent_on)
);
                    
