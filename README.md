# photography-app
I love to take landscape pictures and creating web apps.
This will be my very own site where I can view my photos.

## App
NodeJS React app with Typescript and Sass.

**Created using:**

`npx create-react-app client --typescript && cd client`

`rm src/App.css src/App.test.tsx src/index.css src/logo.svg`

`npm install @types/node`

**Adding TS-Lint:**

`cd client`

`npm install tslint tslint-react`

add tslint.json to client/

**Adding Sass:**

`cd client`

`npm install node-sass`

## Database (MongoDB) + API (Express)

`cd client && npm install axios`

`cd ..`

`mkdir backend && cd backend && npm init`

**Create a new document in mongoDB:**

Start mongo: `mongo`

See current document: `db`

Create new document: `use <new-document-name>`

Update adress in `server.js`: `const dbRoute = 'mongodb://127.0.0.1:27017/<document-name>';`

**Install packages in backend**

`cd backend && npm install mongoose express body-parser morgan cors`

**Add proxy in client/package.json**

`"proxy": "http://localhost:3001"`

**Add concurrently in order to run both client and backend at the same time**

`cd "git-root"`

`npm init -y`

`npm i -S concurrently`

Add a script in `./package.json`
```
"scripts": {
  "start": "concurrently \"cd backend && node server.js\" \"cd client && npm start\""
},
```

**More detailed info on express and mongo:** 

https://medium.com/javascript-in-plain-english/full-stack-mongodb-react-node-js-express-js-in-one-simple-app-6cc8ed6de274

