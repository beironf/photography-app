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

### Photo Editor
<img width="1728" alt="image" src="https://user-images.githubusercontent.com/17812202/177271632-8ae1149c-5bf3-4c00-96bc-0970b67e9562.png">

### Photo Viewer
<p align="middle"/>
  <img width="49.5%" alt="image" src="https://user-images.githubusercontent.com/17812202/181281578-9f2a06fa-87ea-41df-a70e-1d61fd4e8adf.png">
  <img width="49.5%" alt="image" src="https://user-images.githubusercontent.com/17812202/181281240-923b8cca-83a7-4c3e-b979-699fa7cafd68.png">
</p>

