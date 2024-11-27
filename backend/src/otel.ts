import {NodeSDK} from '@opentelemetry/sdk-node';
import {getNodeAutoInstrumentations} from '@opentelemetry/auto-instrumentations-node';
import {OTLPTraceExporter} from '@opentelemetry/exporter-trace-otlp-grpc';
import {OTLPMetricExporter} from '@opentelemetry/exporter-metrics-otlp-grpc';
import {PeriodicExportingMetricReader} from '@opentelemetry/sdk-metrics';

const traceExporter = new OTLPTraceExporter({
    url: 'http://otelcollector:4317',
});

const metricExporter = new OTLPMetricExporter({
    url: 'http://otelcollector:4317',
});

const metricReader = new PeriodicExportingMetricReader({
    exporter: metricExporter,
    exportIntervalMillis: 1000,
});

const sdk = new NodeSDK({
    traceExporter: traceExporter,
    instrumentations: [getNodeAutoInstrumentations()],
    metricReader: metricReader,
});

sdk.start()

process.on('SIGTERM', async () => {
    await sdk.shutdown();
    console.log('OpenTelemetry shut down');
    process.exit(0);
});