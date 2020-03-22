## Spin up Postgres

```
docker-compose up -d
```

### Connect to Postgres
Give the require credential in application.properties.

To connect to postgres you could use any client like DBeaver, HeidiSql etc.

Create tables by running following command:

```
CREATE TABLE reservation (
    id              SERIAL PRIMARY KEY,
    name           VARCHAR(100) NOT NULL
);
```

