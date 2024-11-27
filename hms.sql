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
    accountNumber VARCHAR(50),
    checkupDate DATE
);

SELECT patientID FROM Patient WHERE name IN ("John Doe","Jane Roe","Sam Green");

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
    appointmentID INT AUTO_INCREMENT PRIMARY KEY,
    patientID INT NOT NULL,
    doctorID INT NOT NULL,
    specialization VARCHAR(255) NOT NULL,
    start_time INT,
    end_time INT,
    date DATE NOT NULL,
    FOREIGN KEY (patientID) REFERENCES Patient(patientID),
    FOREIGN KEY (doctorID) REFERENCES Doctor(doctorID)
);


CREATE TABLE MedicalHistory (
    patientId INT PRIMARY KEY, -- Patient ID, serves as the primary key
    patientName varchar(50),
    allergies TEXT,            -- List of allergies
    medications TEXT,          -- Current medications
    pastIllnesses TEXT,        -- Record of past illnesses
    surgeries TEXT,            -- Record of past surgeries
    familyHistory TEXT,        -- Family medical history
    notes TEXT,                -- Additional notes
    lastUpdated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- Auto-update timestamp
);

CREATE TABLE Bills(
    billID INT AUTO_INCREMENT PRIMARY KEY,
    patientID INT NOT NULL,
    amount INT NOT NULL default 0,
    remainingAmount INT NOT NULL default 0,
    accountNumber VARCHAR(50) NOT NULL default '',
    paid boolean NOT NULL DEFAULT FALSE,
    FOREIGN KEY (patientID) REFERENCES Patient(patientID)
);




-- Insert Medical History for Patient ID 1
INSERT INTO MedicalHistory (patientId,patientName, allergies, medications, pastIllnesses, surgeries, familyHistory, notes)
VALUES 
(1, 'John Doe','Peanuts, Pollen', 'Aspirin', 'Chickenpox, Asthma', 'Appendectomy', 'Diabetes', 'Patient is prone to seasonal allergies.'),
(2,'Jane Roe' ,'Penicillin', 'Ibuprofen', 'Measles', 'Tonsillectomy', 'Heart Disease', 'Regular checkups advised for cardiac health.'),
(3,'Sam Green' ,'None', 'Metformin', 'Hypertension', 'None', 'Hypertension, Stroke', 'Patient follows a restricted diet. Monitoring required.'),
(4,'Lisa White' ,'Shellfish', 'Paracetamol', 'None', 'Gallbladder Removal', 'Cancer', 'Patient has a history of malignancies in family.'),
(5,'Paul Black' ,'Latex', 'Insulin', 'Diabetes, Flu', 'None', 'Obesity', 'Patient is insulin-dependent and requires glucose monitoring.');




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

DELIMITER $$

CREATE TRIGGER restrict_doctor_appointment_overlap
BEFORE INSERT ON Appointments
FOR EACH ROW
BEGIN
    DECLARE overlapping_appointments INT;

    -- Check if the doctor already has an appointment at the same time or within 30 minutes
    SELECT COUNT(*) INTO overlapping_appointments
    FROM Appointments
    WHERE doctor_id = NEW.doctor_id
      AND (
           (NEW.time_of_appointment BETWEEN time_of_appointment AND DATE_ADD(time_of_appointment, INTERVAL 29 MINUTE))
           OR
           (time_of_appointment BETWEEN NEW.time_of_appointment AND DATE_ADD(NEW.time_of_appointment, INTERVAL 29 MINUTE))
          );

    -- If there's an overlap, throw an error
    IF overlapping_appointments > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Doctor already has an appointment during this time or within the next 30 minutes.';
    END IF;
END;
$$

DELIMITER ;




INSERT INTO Doctor (name,specialization , email, phoneNumber, hireDate) VALUES
('Dr. Alice Smith','General Medicine' ,'alice.smith@hospital.com', '123-456-7890', '2022-01-15'),
('Dr. Bob Johnson','Cardiologist' ,'bob.johnson@hospital.com', '234-567-8901', '2023-03-20'),
('Dr. Carol Lee','Dermatologist', 'carol.lee@hospital.com', '345-678-9012', '2021-11-25'),
('Dr. David Brown','opthomologist' ,'david.brown@hospital.com', '456-789-0123', '2020-06-10'),
('Dr. Emma Davis','Gynacologist' ,'emma.davis@hospital.com', '567-890-1234', '2023-07-05');



INSERT INTO Patient (name, email, phoneNumber, checkupDate, accountNumber) VALUES
('John Doe', 'john.doe@gmail.com', '123-456-7890', '2024-11-20', 'ACC12345'),
('Jane Roe', 'jane.roe@gmail.com', '234-567-8901', '2024-11-21', 'ACC23456'),
('Sam Green', 'sam.green@gmail.com', '345-678-9012', '2024-11-22', 'ACC34567'),
('Lisa White', 'lisa.white@gmail.com', '456-789-0123', '2024-11-23', 'ACC45678'),
('Paul Black', 'paul.black@gmail.com', '567-890-1234', '2024-11-24', 'ACC56789');



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


select * from appointments; 
-- Insert valid appointments
INSERT INTO Appointments (patientID, doctorID, specialization, date) VALUES
(1, 1, 'General Medicine', '2024-11-17'),
(2, 1, 'General Medicine', '2024-11-17'),
(3, 2, 'Cardiologist', '2024-11-17'),
(4, 2, 'Cardiologist', '2024-11-17'),
(5, 3, 'Dermatologist', '2024-11-17');

INSERT INTO Bills (patientID, amount, remainingAmount, paid) VALUES
(1, 150, 0, TRUE),    -- John Doe, Bill paid
(2, 200, 200, FALSE),   -- Jane Roe, Bill unpaid
(3, 120, 0, TRUE),    -- Sam Green, Bill paid
(4, 180, 100, FALSE),   -- Lisa White, Bill unpaid
(5, 220, 0, TRUE);    -- Paul Black, Bill paid


-- Attempt to insert overlapping appointment (this will fail)
INSERT INTO Appointments (patient_id, doctor_id, time_of_appointment) VALUES
(2, 1, '2024-11-17 10:15:00'); -- Overlaps with the 10:00:00 appointment


select * from MedicalHistory;
