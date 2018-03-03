#whatsnext-swagger
Rest API using spring


##Gradle

Run the application using 

```
./gradlew bootRun
```

Build the jar

```
./gradlew build
```

Run the Jar after build

```
java -jar build/libs/whatsnext-swagger-1.0.jar
```

#Run the app

```
 http://localhost:8080/swagger-ui.html
 ```
 
#Twitter Controller

Prerequisite

Check the image registerTwitter in images folder on how to create an app in Twitter

```
Go to https://apps.twitter.com/
Once you create an app
Click on Keys and Access Token
Copy and paste it in /resources/application.properties
1. Consumer Key (API Key)
2. Consumer Secret (API Secret)
3. Access Token
4. Access Token Secret
```


#Google(Youtube and Drive) Controller
Prerequisite:
Create a new project in https://console.developers.google.com


Remove local credentials from your machine and start from fresh

```
cd ~/.credentials/drive-java-quickstart
rm StoredCredential
cd ~/.credentials/youtube-java-quickstart
rm StoredCredential
```


#Facebook Controller
Create a new app in https://developers.facebook.com/
Generate appId and appSecret and provide to admin team

For accessToken please request admin team. Once you recieve the accessToken call the API's accordingly



