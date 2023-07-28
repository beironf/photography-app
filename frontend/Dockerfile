FROM node:17-alpine

WORKDIR /app

COPY package.json .

RUN npm install --legacy-peer-deps

RUN apk --no-cache add curl

COPY . .

EXPOSE 3000

CMD ["npm", "start"]