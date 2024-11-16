use hms;

create table Doctor(
	doctorID int identity(1,1) Primary key not null,
	name varchar(50) not null,
	email varchar(100) not null,
	phoneNumber varchar(50) not null,
	hireDate date not null
);

create table Patient(
	patientID int identity(1, 1) primary key not null,
	name varchar(50) not null,
	email varchar(100) not null,
	phoneNumber varchar(50) not null,
	checkupDate date not null
);

create table Admin(
	adminID int identity(1,1) Primary key not null,
	name varchar(50) not null,
	email varchar(100) not null,
	phoneNumber varchar(50) not null,
	hireDate date not null
);

create table Appointments(
	appointmentID int identity(1, 1) primary key not null
);