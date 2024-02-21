DROP TABLE IF EXISTS DEPT_PROJECT;
DROP TABLE IF EXISTS PROJECT;
DROP TABLE IF EXISTS DEPARTMENT;
DROP TABLE IF EXISTS EMPLOYEE;

CREATE TABLE EMPLOYEE(
	EMPLOYEE_ID INT NOT NULL AUTO_INCREMENT,
	EMPLOYEE_NAME VARCHAR(255),
	EMPLOYEE_EMAIL VARCHAR(255),
	JOINING_DATE  DATETIME,
	PRIMARY KEY (EMPLOYEE_ID),
	UNIQUE KEY (EMPLOYEE_EMAIL)
);

CREATE TABLE DEPARTMENT(
	EMPLOYEE_ID INT NOT NULL,
	DEPT_ID    INT NOT NULL AUTO_INCREMENT,
	DEPT_NAME VARCHAR(255),
	PRIMARY KEY (DEPT_ID),
	FOREIGN KEY (EMPLOYEE_ID) REFERENCES EMPLOYEE (EMPLOYEE_ID) ON DELETE CASCADE
);

CREATE TABLE PROJECT(
	PROJECT_ID INT NOT NULL AUTO_INCREMENT,
	PROJECT_NAME VARCHAR(255),
	PRIMARY KEY (PROJECT_ID)
);

CREATE TABLE DEPT_PROJECT(
	DEPT_ID    INT NOT NULL,
	PROJECT_ID INT NOT NULL,
	PRIMARY KEY(DEPT_ID,PROJECT_ID),
	FOREIGN KEY (DEPT_ID) REFERENCES DEPARTMENT (DEPT_ID) ON DELETE CASCADE,
	FOREIGN KEY (PROJECT_ID) REFERENCES PROJECT (PROJECT_ID) ON DELETE CASCADE

);