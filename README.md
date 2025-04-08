# Prerequisites

- Docker installation
- Java17 Runtime (only for starting the app locally)

## Running Locally
Create a postgres container with following command:

```
docker run -e POSTGRES_USER=user \
           -e POSTGRES_PASSWORD=password \
           -e POSTGRES_DB=digital_assistant \
           --name digital_assistant_db \
           -p 5432:5432 \
           -d postgres
```

Start application within IDE (For VS Code Springboot extension should be added) 
With the application running, access [API Docu](http://localhost:8080/swagger-ui/index.html) for the swagger documentation

## Running in Containers

For starting both the containized application as the postgres container run following container: 

```
docker-compose up
```

# Testing request with REST Client (VS Code)
With the extension [REST Client](https://marketplace.visualstudio.com/items/?itemName=humao.rest-client) open file __requests/assistants.rest__ and run the report, the name of the service and description may be changed
