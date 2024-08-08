# Base image
FROM node:22-alpine3.19 as build-stage

# Set working directory
WORKDIR /app

# Copy package.json and package-lock.json
COPY package.json package-lock.json ./

# Install dependencies
RUN npm ci

# Copy the source code
COPY . ./

ENV NODE_ENV=production
# Build the Next.js app with static export
RUN npm run build

USER nginx

# Intermediate image for Nginx
FROM nginx:1.27.0-alpine3.19

# Copy the static files from the build-stage
COPY --from=build-stage /app/build /usr/share/nginx/html

# Copy custom Nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose port 4001
EXPOSE 4001

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]