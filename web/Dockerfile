FROM node:12 AS builder

COPY ./ /web
WORKDIR /web

RUN npm --version

RUN npm install
RUN npm run build-testnet


FROM bitnami/nginx:latest 
COPY --from=builder /web/dist/* /app/
