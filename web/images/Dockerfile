FROM node:latest AS builder
COPY ./ ./
WORKDIR /src

RUN npm install
RUN npm run build-testnet


FROM bitnami/nginx:latest 
COPY --from=builder /src/dist/* /app/
