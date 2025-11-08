# Notes App

## Description
Notes App is a simple application where users can create and manage notes. Each note has a title, text, created date, and tags like BUSINESS, PERSONAL, or IMPORTANT. You can filter notes by tags and see the newest notes first.

## How to Run
The easiest way to run the app is using **Docker Compose**.

1. Make sure **Docker** is installed and running on your machine. You can download it here: [Docker Official Website](https://www.docker.com/get-started)
2. Create a `.env` file in the docker-compose folder. You can use the content in the example `.env.sample` file and put your own values.
3. Move to docker-compose folder. Run the app with Docker Compose command:

```bash
docker-compose up -d
```

## Try by yourself
I have created a Postman collection for you to test the API endpoints easily. 
If you running the app using Spring Boot, replace the url in application.yml, because real url is hidden for security reasons.
You can find the collection file here: [Notes App Postman Collection](https://web.postman.co/workspace/My-Workspace~094913b8-3731-4cce-8108-9a9ee90872b2/collection/25455394-348db0dd-b27c-4647-84b6-19e53d61076b?action=share&source=copy-link&creator=25455394)
