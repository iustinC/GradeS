# GradeS
GradeS


GradeS is a platform where teachers can add assignments, students, grades or reports, and students can check the assignments, grades or other information.

There are 2 account types:
  1. Teacher
  
     As teacher you can:
        - add/delete/update students, assignments and grades
        - view students/assignments/grades paginated or filtered
        - each time a grade is added/updated/deleted the student is announced by mail that contains the operation
        - export reports about grades: the hardest assignment, the top of groups , etc
  2. Student
  
      As student you can:
      - view all assignments
      - view your grades with observations from teacher
      - other information about you as student : your name, email, group, current grade, etc
# Table of Contents
* [Usage](#usage)
* [Requirements](#requirements)
* [First Use Instructions](#first-use-instructions)
* [Database](#database)


# USAGE
First time you can register 

![registerframe](https://user-images.githubusercontent.com/21144919/35773814-64c5a42e-0965-11e8-9cf1-591556679a89.gif)

or login if you have an account

![loginframe2](https://user-images.githubusercontent.com/21144919/35773799-183430bc-0965-11e8-9944-d9323cb2bae2.gif)

If you are logged as student you can see all assignments or your grades

![studentteme](https://user-images.githubusercontent.com/21144919/35700496-d6670986-079b-11e8-92ce-5604c71ec86e.png)

also you can check your information

![studentinformatii](https://user-images.githubusercontent.com/21144919/35700522-e5e5a3f4-079b-11e8-86f5-2174bb9e4380.png)

If you are logged as teacher you can select what category you want to check

![profesoroptiuni](https://user-images.githubusercontent.com/21144919/35700574-06ed6cb2-079c-11e8-9986-c605f13936cf.png)

On students tab you can add/update/delete students or you can filter them(you can use compound filters)

![profesorstudents](https://user-images.githubusercontent.com/21144919/35700815-ba9f6da0-079c-11e8-8b64-617995df4cfe.png)

On assignments tab you can add/update/delete assignments or you can filter them(you can use compound filters)

![profesorteme](https://user-images.githubusercontent.com/21144919/35700853-db3df8f6-079c-11e8-8578-857940a8546e.png)

On grades tab tab you can add/update/delete grades or you can filter them(you can use compound filters) or generate reports about current grades from database

![profesornote](https://user-images.githubusercontent.com/21144919/35700900-fa1b2b2c-079c-11e8-893d-91291263ad4f.png)

![profesoraddnota](https://user-images.githubusercontent.com/21144919/35700902-fbbb520e-079c-11e8-98a6-4ed17bba6962.png)

![graficmediestudenti](https://user-images.githubusercontent.com/21144919/35700904-fcec2090-079c-11e8-8c2b-917853bfcfa0.png)

![grafictopulgrupelor](https://user-images.githubusercontent.com/21144919/35700908-fed0da7c-079c-11e8-81f8-46c9665cde6d.png)

# Requirements

In order to successfully run this  app you need a few things:

1. Java 1.8
2. JavaFX 8
3. [Jars.zip](https://github.com/farma1738/GradeS/files/1687100/Jars.zip)(include JARs and folder in modules)
4. SQL Server (i used SQL Server Management Studio)


# First Use Instructions
1. Clone the GitHub repo to your computer
2. Import the project in IntelliJ IDEA or any other IDE of your choice
3. Import JARs
4. Connect to database


# Database

The application connects to a SQL Server database that holds student, assignment, grades and accounts information. The information that it holds is as follows:

1. Studenti
    - idStudent
    - nume
    - email
    - cadruDidactic
2. Teme
    - numarTema
    - cerinta
    - deadline
3. Note
    - idStudent
    - numarTema
    - valoare
    - saptamanaPredarii
4. Utilizatori
    - username
    - pass
    - email
    - prioritate
    
 You can create this database on your own with the following SQL:
```
 IF OBJECT_ID('Studenti', 'U') IS NOT NULL
	DROP TABLE Studenti
  CREATE TABLE Studenti(
	idStudent INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
	nume VARCHAR(255),
	grupa INT, 
	email VARCHAR(255) UNIQUE,
	cadruDidactic VARCHAR(255)
)

IF OBJECT_ID('Teme', 'U') IS NOT NULL
	DROP TABLE Teme
CREATE TABLE Teme(
	numarTema INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
	cerinta VARCHAR(255) UNIQUE,
	deadline INT
)

IF OBJECT_ID('Note', 'U') IS NOT NULL
	DROP TABLE Note
CREATE TABLE Note(
	idStudent INT FOREIGN KEY REFERENCES dbo.Studenti(idStudent) ON DELETE CASCADE,
	numarTema INT FOREIGN KEY REFERENCES dbo.Teme(numarTema) ON DELETE CASCADE,
	PRIMARY KEY (idStudent,numarTema),
	valoare INT,
	saptamanaPredarii INT
)

IF OBJECT_ID('Utilizatori', 'U') IS NOT NULL
	DROP TABLE Utilizatori
CREATE TABLE Utilizatori(
	username VARCHAR(255) PRIMARY KEY,
	pass VARCHAR(255) NOT NULL,
	email VARCHAR(255) NOT NULL,
	prioritate VARCHAR(255) NOT NULL
)
```


