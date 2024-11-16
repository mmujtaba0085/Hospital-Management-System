CREATE DATABASE HMS;
USE HMS;

-- Create the login table
CREATE TABLE login (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('Patient', 'Doctor', 'Admin', 'Receptionist') NOT NULL
);

-- Create the Doctor table
CREATE TABLE Doctor (
    doctorID INT AUTO_INCREMENT PRIMARY KEY,
    specialization varchar(50) NOT NULL,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phoneNumber VARCHAR(50) NOT NULL,
    hireDate DATE NOT NULL
);

select doctorId,specialization,name,phoneNumber,hireDate from doctor where email="alice.smith@hospital.com";
CREATE TABLE Receptionist (
    receptionistID INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phoneNumber VARCHAR(50) NOT NULL,
    hireDate DATE NOT NULL
);

-- Create the Patient table
CREATE TABLE Patient (
    patientID INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phoneNumber VARCHAR(50) NOT NULL,
    checkupDate DATE NOT NULL
);

-- Create the Admin table
CREATE TABLE Admin (
    adminID INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phoneNumber VARCHAR(50) NOT NULL,
    hireDate DATE NOT NULL
);

-- Create the Appointments table
CREATE TABLE Appointments (
    appointmentID INT AUTO_INCREMENT PRIMARY KEY
);

-- Trigger to add doctors to the login table automatically
DELIMITER $$

CREATE TRIGGER after_doctor_insert
AFTER INSERT ON Doctor
FOR EACH ROW
BEGIN
    INSERT INTO login (username, password, role)
    VALUES (NEW.email, 'default_password', 'Doctor');
END;
$$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER after_patient_insert
AFTER INSERT ON Patient
FOR EACH ROW
BEGIN
    INSERT INTO login (username, password, role)
    VALUES (NEW.email, 'default_password', 'Patient');
END;
$$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER after_admin_insert
AFTER INSERT ON Admin
FOR EACH ROW
BEGIN
    INSERT INTO login (username, password, role)
    VALUES (NEW.email, 'default_password', 'Admin');
END;
$$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER after_receptionist_insert
AFTER INSERT ON Receptionist
FOR EACH ROW
BEGIN
    INSERT INTO login (username, password, role)
    VALUES (NEW.email, 'default_password', 'Receptionist');
END;
$$

DELIMITER ;




INSERT INTO Doctor (name,specialization , email, phoneNumber, hireDate) VALUES
('Dr. Alice Smith','General Medicine' ,'alice.smith@hospital.com', '123-456-7890', '2022-01-15'),
('Dr. Bob Johnson','Cardiologist' ,'bob.johnson@hospital.com', '234-567-8901', '2023-03-20'),
('Dr. Carol Lee','Dermatologist', 'carol.lee@hospital.com', '345-678-9012', '2021-11-25'),
('Dr. David Brown','opthomologist' ,'david.brown@hospital.com', '456-789-0123', '2020-06-10'),
('Dr. Emma Davis','Gynacologist' ,'emma.davis@hospital.com', '567-890-1234', '2023-07-05');



INSERT INTO Patient (name, email, phoneNumber, checkupDate) VALUES
('John Doe', 'john.doe@gmail.com', '123-456-7890', '2024-11-20'),
('Jane Roe', 'jane.roe@gmail.com', '234-567-8901', '2024-11-21'),
('Sam Green', 'sam.green@gmail.com', '345-678-9012', '2024-11-22'),
('Lisa White', 'lisa.white@gmail.com', '456-789-0123', '2024-11-23'),
('Paul Black', 'paul.black@gmail.com', '567-890-1234', '2024-11-24');


INSERT INTO Admin (name, email, phoneNumber, hireDate) VALUES
('Admin A', 'admin.a@hospital.com', '111-222-3333', '2019-10-01'),
('Admin B', 'admin.b@hospital.com', '222-333-4444', '2020-11-15'),
('Admin C', 'admin.c@hospital.com', '333-444-5555', '2021-09-20'),
('Admin D', 'admin.d@hospital.com', '444-555-6666', '2022-08-05'),
('Admin E', 'admin.e@hospital.com', '555-666-7777', '2023-07-30');

INSERT INTO Receptionist (name, email, phoneNumber, hireDate) VALUES
('Rachel Green', 'rachel.green@hospital.com', '789-123-4560', '2021-10-15'),
('Monica Geller', 'monica.geller@hospital.com', '678-234-5671', '2020-12-01'),
('Phoebe Buffay', 'phoebe.buffay@hospital.com', '567-345-6782', '2022-03-20'),
('Chandler Bing', 'chandler.bing@hospital.com', '456-456-7893', '2023-01-25'),
('Joey Tribbiani', 'joey.tribbiani@hospital.com', '345-567-8904', '2023-11-05');





