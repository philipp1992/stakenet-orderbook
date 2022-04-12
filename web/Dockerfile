FROM node:latest AS builder
COPY ./ ./


RUN npm install
RUN npm run build-testnet


FROM bitnami/nginx:latest 
COPY --from=builder /dist/* /app/
