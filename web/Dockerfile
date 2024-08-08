# Base image
FROM node:20-alpine as build-stage

# Set working directory
WORKDIR /app

# Copy package.json and package-lock.json
COPY package.json package-lock.json ./

# Install dependencies
RUN npm ci

# Copy the source code
COPY . ./

# Build the Next.js app with static export
RUN npm run build

# Intermediate image for Nginx
FROM nginx:alpine

# Copy the static files from the build-stage
COPY --from=build-stage /app/build /usr/share/nginx/html

# Copy custom Nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose port 80
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]