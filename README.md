# My Photography App

I love to take landscape pictures and creating web apps.
This will be my very own site where I can share my photos.

## Example Images

### Showroom/Gallery

<p align="middle"/>
  <img width="79%" alt="image" src="https://github.com/beironf/photography-app/assets/17812202/f78a62d3-8331-45ee-bc1f-8bb26e8d0c42">
  <img width="20.3%" alt="image" src="https://github.com/beironf/photography-app/assets/17812202/c2cb5f5e-1097-47be-a349-2ce2203281a1">
</p>

### Photo Viewer

<p align="middle"/>
  <img width="49.5%" alt="image" src="https://github.com/beironf/photography-app/assets/17812202/86b33401-5ac9-43a0-8255-8b71f197e634">
  <img width="49.5%" alt="image" src="https://github.com/beironf/photography-app/assets/17812202/a1b8ddc7-301c-438c-91ab-9428e1932a90">
</p>

### Photo Manager

<img width="1728" alt="image" src="https://github.com/beironf/photography-app/assets/17812202/4aacdcdc-7bfb-483b-bd09-cf2498822d08">

## Run Locally

> _Note_, If this is the first time running this project you would need to generate the `api`-image first using:
>
> ```
> cd backend
> sbt Docker/publishLocal
> ```
>
> The easiest way is to use docker from project root:

```
docker-compose up -d
```

## Development

### Frontend App

NodeJS React app with Typescript and Sass.

#### Created using:

```
npx create-react-app frontend --typescript
```

#### Start:

```
./start-frontend.sh
```

### Backend API

A Scala app (built using sbt) using Akka HTTP and Tapir. The code architecture is inspired by the "hexagonal" (or "ports and adapters") architecture. Slick is used to connect to the DB.

- Handling all photos and their metadata.
- An API for uploading and fetching images. EXIF data will be stored before the images are resized on upload.

#### Start:

```
./start-api.sh
```

### PostgreSQL Database with Docker

It would probably have made more sense to run this app using for example MongoDB (or similar NoSQL), but I went for a PostgreSQL database because it was more familiar and easy to use with Heroku.

#### Initiate/Start:

The DB is specified in the `docker-compose.yaml` file (services: `photography-db`) where we create a PostgreSQL-container with the database `photography_db` inside.

```
./start-db.sh
```

#### Connect:

The DB is hosted inside a Docker container and exposed on the port `4001`.

```
psql -h localhost -p 4001 -U postgres photography_db
```

## Deploying

### Heroku

The Scala backend API and the PostgreSQL database are both hosted by Heroku.

#### Deploy API

```
TODO: fill in here...
```

#### Connect to Database

```
TODO: fill in here...
photography-db --user=postgres --database=photography_db
```

### Firebase

The frontend app and the cloud storage (for images) is hosted by [Firebase](https://console.firebase.google.com).

#### Deploy Frontend

```
./deploy-frontend.sh
```
