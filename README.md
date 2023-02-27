# CV-Generator-Spring-Boot
A CV Generator using Spring Boot, Spring MVC, Spring Security and Hibernate. The data is saved in a MySQL database. ApachePDFBox is used to save the CV as a PDF file. In order to use the application, the user must create an account. Then, he is able to save or modify his data. The application contains sections for the most common details found in a resume, allowing the user to easily create his CV. When the user is happy with the data he has provided he can choose which sections he wants to include in his resume, name them and generate his CV by pressing a button.


How to run:
* run the SQL script to create the database
* fill in the database and the email properties in the application.properties file
* run CvgeneratorApplication.java


Features: 
* account creation and management: verification, username/password recovery, change email/password (a confirmation email will be sent to verify the user's identity), delete account
* save and edit common data found in a resume: profile picture, contact information, description, education, experience, languages, projects, hobbies, soft skills, hard skills
* generate a CV by choosing a font, a color, a style and the included sections, then download it
* administration page: all the users are listed, they can be searched by username, they can be suspended/unsuspended or deleted


Screenshots:

* CV generation
![generatecv](https://user-images.githubusercontent.com/125903019/220716104-0db9ab52-1a7a-42b7-9f77-b2e9d4e3c009.png)

* account management
![manageaccount](https://user-images.githubusercontent.com/125903019/220716110-bae9dc34-5790-42f5-a32d-eec67c2a786a.png)

* menu
![menu](https://user-images.githubusercontent.com/125903019/220716115-712ccec9-3984-4d35-94f1-baceb460368e.png)

* uploading picture
![uploadingpicture](https://user-images.githubusercontent.com/125903019/221522717-f2e3e33c-8a36-41f7-b951-32903a8b8843.png)


* sign in page
![siginpage](https://user-images.githubusercontent.com/125903019/220716128-03825a61-4362-481e-9a14-b843e46b90e8.png)

* generated CVs
![generatedcv1](https://user-images.githubusercontent.com/125903019/221523479-7eb6f92f-7691-482c-a4d6-5fe1abc0ca7b.png)


![generatedcv2](https://user-images.githubusercontent.com/125903019/221523507-b6bef615-b19b-447f-aae5-327d22a05f09.png)


* adding education
![addingeducation](https://user-images.githubusercontent.com/125903019/221523530-9e2171e2-3e71-4f71-99b3-f82b9cdd4245.png)


* admin page
![adminpage](https://user-images.githubusercontent.com/125903019/220716071-fee39be9-d70a-46ea-ad67-1e848cfac15d.png)

* CV generated page
![cvgenerated](https://user-images.githubusercontent.com/125903019/220716083-86d6eb10-9e19-4d3c-b74d-5c2a180b6f4c.png)

* account deleted
![deleteaccount](https://user-images.githubusercontent.com/125903019/220716089-1bca3740-01f5-4f06-8379-dc506ad2d276.png)

* user details
![userdetailpage](https://user-images.githubusercontent.com/125903019/221523584-87c5c359-87ce-4f9d-9eb0-f5812e840dcd.png)


* education list
![educationlist](https://user-images.githubusercontent.com/125903019/221523701-c90ffd1b-c727-4e9e-9d44-3deef48bf187.png)

