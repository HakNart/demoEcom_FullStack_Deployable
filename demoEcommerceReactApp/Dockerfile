FROM node:18-alpine

WORKDIR /home/app

COPY package.json .
# COPY entrypoint.sh /entrypoint.sh
# RUN chmod +x /entrypoint.sh

RUN npm install

COPY . .

EXPOSE 3000

# ENTRYPOINT ["/entrypoint.sh"]

CMD ["npm", "run", "dev"]