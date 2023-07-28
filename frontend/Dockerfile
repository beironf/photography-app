FROM node:17-alpine

WORKDIR /app

COPY package.json .

RUN npm install --legacy-peer-deps

RUN apt-get update; apt-get install -y curl

COPY . .

EXPOSE 3000

CMD ["npm", "start"]