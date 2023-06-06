# photography-app
I love to take landscape pictures and creating web apps.
This will be my very own site where I can view my photos.

## Frontend App
NodeJS React app with Typescript and Sass.

#### Created using:
```
npx create-react-app frontend --typescript
```

#### Start:
```
./start-frontend.sh
```

## Backend APIs
Scala app (built using sbt) using Akka HTTP and Tapir. The code architecture is inspired by the "hexagonal" (or "ports and adapters") architecture. Slick is used to connect to the DB.

### Photo API
An API for handling all photos and their metadata.

#### Start:
```
./start-photo-api.sh
```

### Image API
An API for uploading and fetching images. EXIF data will be stored before the images are resized on upload.

#### Start:
```
./start-image-api.sh
```

## MySQL Database with Docker
Common webhotels usually have support for MySQL so I went for this even though it probably would have made more sense to run this app using for example MongoDB (or similar NoSQL).

#### Initiate/Start:
The DB is specified in the `docker-compose.yaml` file (services: `db`) where we create a MySQL-container (`photography-db`) with the database `photography_db` inside.
```
docker-compose up -d db
```

#### Connect:
The DB is hosted inside a Docker container and exposed on the port `4001`. For some reason the `--protocol=tcp` was needed when using "localhost" (`127.0.0.1` works without whis flag).
```
mysql -u root -h localhost -P 4001 -D photography_db --protocol=tcp
```

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
<img width="1728" alt="image" src="https://github.com/beironf/photography-app/assets/17812202/f2e5de8f-8d22-4885-9e25-75dc5683a38a">
<img width="1728" alt="image" src="https://github.com/beironf/photography-app/assets/17812202/4aacdcdc-7bfb-483b-bd09-cf2498822d08">

