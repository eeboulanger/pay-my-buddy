# Pay my buddy

## Introduction:
An application for transfering money between friends:
1. Create an account with new credentials or use your social media account 
2. Add friends to your network 
3. Transfer money to your friends!

## What you need:
* Spring Boot 3.3.0
* Java 21
* Thymeleaf
* Data JPA
* Spring Security
* Oauth2 client
* MySQL driver
* Lombok (optional)

#### Optional for testing:
* Hibernate Validator
* Selenium
* WebDriverManager

## Setup with Intellij IDE:
1. Create project from Initializr: File > New > project > Spring Initializr
Add lib repository into pom.xml

2. Clone the project:
git clone https://github.com/eeboulanger/pay-my-buddy.git

## Database setup

### MPD
![MPD](https://github.com/eeboulanger/pay-my-buddy/blob/dev/src/main/resources/readme/MPD.png)



1. **Create database**
* Run sql script to create schema data/pay_my_buddy_db.sql
* Run sql script to poulate database for testing data/pay_my_buddy_data.sql (optional) 

2. **Application.properties**
* Don't forget to add your own mysql username and password. 
* Also add client id and secret for Google and GitHub to allow sign in with your social media account! 
