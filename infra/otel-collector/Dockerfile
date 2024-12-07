# Stage 1: Builder
FROM golang:1.23-alpine3.20 AS builder

ARG OTEL_VERSION=0.114.0

WORKDIR /build

# Install the builder tool
RUN go install go.opentelemetry.io/collector/cmd/builder@v${OTEL_VERSION}

# Copy the manifest file and other necessary files
COPY manifest.yaml .

# Build the custom collector
RUN CGO_ENABLED=0 builder --config=manifest.yaml

# Stage 2: Final Image
FROM alpine:3.20

WORKDIR /

# Copy the generated collector binary from the builder stage
COPY --from=builder /build/otelcol-custom .

# Expose necessary ports
EXPOSE 4317/tcp 4318/tcp

# Set the default command
ENTRYPOINT ["/otelcol-custom"]
