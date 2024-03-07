package com.azure.otel;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.baggage.propagation.W3CBaggagePropagator;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.exporter.logging.LoggingMetricExporter;
import io.opentelemetry.exporter.otlp.http.logs.OtlpHttpLogRecordExporter;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.semconv.ResourceAttributes;

public class OtelConfig {

	public static OpenTelemetry openTelemetry() {
		Resource resource = Resource.getDefault().toBuilder().put(ResourceAttributes.SERVICE_NAME, "azure-fn-otel")
				.put(ResourceAttributes.SERVICE_VERSION, "0.1.0").build();

		SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
				.addSpanProcessor(
						BatchSpanProcessor.builder(OtlpHttpSpanExporter.builder().setEndpoint("/api/v2/otlp/v1/traces")
								.addHeader("Authorization", "Api-Token ").build()).build())
				.setResource(resource).build();

		SdkMeterProvider sdkMeterProvider = SdkMeterProvider.builder()
				.registerMetricReader(PeriodicMetricReader.builder(LoggingMetricExporter.create()).build())
				.setResource(resource).build();

		SdkLoggerProvider sdkLoggerProvider = SdkLoggerProvider.builder()
				.addLogRecordProcessor(BatchLogRecordProcessor.builder(OtlpHttpLogRecordExporter.builder()
						.setEndpoint("/api/v2/otlp/v1/logs").addHeader("Authorization", "Api-Token ").build()).build())
				.setResource(resource).build();

		OpenTelemetry openTelemetry = OpenTelemetrySdk.builder().setTracerProvider(sdkTracerProvider)
				.setMeterProvider(sdkMeterProvider).setLoggerProvider(sdkLoggerProvider)
				.setPropagators(ContextPropagators.create(TextMapPropagator
						.composite(W3CTraceContextPropagator.getInstance(), W3CBaggagePropagator.getInstance())))
				.buildAndRegisterGlobal();
		System.out.println("openTelemetry.getLogsBridge() ||  " + openTelemetry.getLogsBridge());

		return openTelemetry;
	}

	public static void maybeRunWithSpan(Runnable runnable, boolean withSpan) {
		if (!withSpan) {
			runnable.run();
			return;
		}
		Span span = GlobalOpenTelemetry.getTracer("my-tracer").spanBuilder("my-span").startSpan();
		try (Scope unused = span.makeCurrent()) {
			runnable.run();
		} finally {
			span.end();
		}
	}

}
