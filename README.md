# My Photography App
I love to take landscape pictures and creating web apps.
This will be my very own site where I can view my photos.

## Example Images

#### Showroom/Gallery
<p align="middle"/>
  <img width="79%" alt="image" src="https://github.com/beironf/photography-app/assets/17812202/f78a62d3-8331-45ee-bc1f-8bb26e8d0c42">
  <img width="20.3%" alt="image" src="https://github.com/beironf/photography-app/assets/17812202/c2cb5f5e-1097-47be-a349-2ce2203281a1">
</p>

#### Photo Viewer
<p align="middle"/>
  <img width="49.5%" alt="image" src="https://github.com/beironf/photography-app/assets/17812202/86b33401-5ac9-43a0-8255-8b71f197e634">
  <img width="49.5%" alt="image" src="https://github.com/beironf/photography-app/assets/17812202/a1b8ddc7-301c-438c-91ab-9428e1932a90">
</p>

#### Photo Manager
<img width="1728" alt="image" src="https://github.com/beironf/photography-app/assets/17812202/4aacdcdc-7bfb-483b-bd09-cf2498822d08">

## Run Locally
The easiest way is to use docker from project root:
```
docker-compose up -d
```

# Development

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

## PostgreSQL Database with Docker
It would probably have made more sense to run this app using for example MongoDB (or similar NoSQL), but I went for a SQL database because it was more familiar.

#### Initiate/Start:
The DB is specified in the `docker-compose.yaml` file (services: `photography-db`) where we create a PostgreSQL-container with the database `photography_db` inside.
```
docker-compose up -d photography-db
```

#### Connect:
The DB is hosted inside a Docker container and exposed on the port `4001`.
```
psql -h localhost -p 4001 -U postgres photography_db
```

## Google Cloud

#### Connect to Database
```
gcloud sql connect photography-db --user=postgres --database=photography_db
```

#### Creating Service Accounts - Workload Identity
```
// add a Google Service Account (GSA)
gcloud iam service-accounts create <gsa-name>

// add a Kubernetes Service Account (KSA)
kubectl create serviceaccount --namespace <k8s-namespace> <ksa-name>

// allow the KSA to use the GSA
gcloud iam service-accounts add-iam-policy-binding --role roles/iam.workloadIdentityUser --member "serviceAccount:beiron-photography-app.svc.id.goog[<k8s-namespace>/<ksa-name>]" <gsa-name>@beiron-photography-app.iam.gserviceaccount.com

// annotate the KSA with the GSA reference
kubectl annotate serviceaccount --namespace <k8s-namespace> <ksa-name> iam.gke.io/gcp-service-account=<gsa-name>@beiron-photography-app.iam.gserviceaccount.com
```

#### Grant access in DB
```
GRANT pg_read_all_data TO <db-user>;
GRANT pg_write_all_data TO <db-user>;
```