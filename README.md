# Backend API for Pet Adoption APP (Version 2.0)

<b>Students:</b> João Vitor Donaton <br>
<b>Instructor:</b> Vinicius Godoy <br>
<b>[Link to presentation video (PORTUGUESE)](https://www.youtube.com/watch?v=cU351oYpKuI)</b> <br>
<b>[Link to powerpoint presentation (PORTUGUESE)](https://docs.google.com/presentation/d/13cT0ZHaYo5hq0otKDznQIyC0bLPGd1kjzCT8pPLYeIM/edit?usp=sharing)</b> <br>

### **Compilation Instructions:**
 - Clone repository  
 - Create file named apikeys.properties in <i>src/main/resources</i>, with the property ```keys.googleApi``` (key to access google Geocoding API)
 - Configure database (MySQL) through application.properties.
   - spring.datasource.url=DATABASE_URL
   - spring.datasource.username=USERNAME
   - spring.datasource.password=PASSWORD
 - Use the command ```./mvnw spring-boot:run``` or use Intellij to compile and run

### **Configurations:**

- <i>apikeys.properties</i> (not in git)
  - Property: keys.google-api (google API key to query location data)
- <i>bootstrapdata.properties</i>
    - Data for database bootstrapping
    - Details in <i>BootstrapSettings.java</i>
- <i>location.properties</i>
  - Third party API URLs
  - <i>LocationUtils.java</i> replaces placeholder data (ADDRESS, KEY e o CEP)
- <i>messages.properties</i>
  - Standardized error messages to return in case of exceptions
- <i>security.properties</i>
  - Secret e Issuer to generate JWT tokens

### **The API features endpoints for:**

- Account management and authentication
- Creation of profiles with location data (google geocoding API and viacep API) and adoption preferences
- Pet enrollment and management
- Matching system to find pets that fit the users profile
- Sending and management of adoption requests for pets
- Some endpoints were removed in version 2.0 (I chose to focus on other features, as implementing these endpoints is basically just doing more of the same)
  - ~~Creation of NGOs, government organizations and user organizations~~~
  - ~~Creation of adoption campaigns~~
  - ~~Adoption campaign blogposts~~
