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
![pictureupload](https://user-images.githubusercontent.com/125903019/220716121-10af9507-0781-4696-8676-e02382e2771c.png)

* sign in page
![siginpage](https://user-images.githubusercontent.com/125903019/220716128-03825a61-4362-481e-9a14-b843e46b90e8.png)

* generated CVs
![snowcv1](https://user-images.githubusercontent.com/125903019/220716137-fc3ed1d6-96b3-437a-a0b7-b1070cf8a530.png)


![snowcv2](https://user-images.githubusercontent.com/125903019/220716144-8d66cd7e-ee87-4f89-b5cb-010a9c399b38.png)

* adding education
![addeducation](https://user-images.githubusercontent.com/125903019/220716063-c2e98898-5635-4698-a6ce-f3e7c506edbb.png)

* admin page
![adminpage](https://user-images.githubusercontent.com/125903019/220716071-fee39be9-d70a-46ea-ad67-1e848cfac15d.png)

* CV generated page
![cvgenerated](https://user-images.githubusercontent.com/125903019/220716083-86d6eb10-9e19-4d3c-b74d-5c2a180b6f4c.png)

* account deleted
![deleteaccount](https://user-images.githubusercontent.com/125903019/220716089-1bca3740-01f5-4f06-8379-dc506ad2d276.png)

* user details
![details](https://user-images.githubusercontent.com/125903019/220716092-7840e66e-dc02-48fa-af3b-15a3782c1267.png)

* education list
![educationlist](https://user-images.githubusercontent.com/125903019/220716098-3b496617-8a31-4c57-a1db-7307e59a3957.png)
