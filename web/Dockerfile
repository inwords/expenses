# Base image
FROM node:20-alpine3.20 AS build-stage

# Set working directory
WORKDIR /app

# Copy package.json and package-lock.json
COPY package.json package-lock.json ./

COPY --chown=node:node . .

# Install dependencies
RUN npm ci

# Copy the source code
COPY . ./

ENV NODE_ENV=production
# Build the Next.js app with static export
RUN npm run build

USER nginx

# Intermediate image for Nginx
FROM nginx:1.27.3-alpine3.20

# Copy the static files from the build-stage
COPY --from=build-stage /app/build /usr/share/nginx/html

# Copy custom Nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose port 4001
EXPOSE 4001

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]
