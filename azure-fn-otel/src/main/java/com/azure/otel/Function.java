package com.azure.otel;

import java.util.Optional;

import org.slf4j.LoggerFactory;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.logs.Severity;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {

	// static Logger logger = LoggerFactory.getLogger(Function.class);
	private static final org.slf4j.Logger slf4jLogger = LoggerFactory.getLogger("slf4j-logger");
	private static io.opentelemetry.api.logs.Logger customAppenderLogger = null;
	static {
		OpenTelemetry openTelemetry = null;

		try {
			openTelemetry = OtelConfig.openTelemetry();
			customAppenderLogger = openTelemetry.getLogsBridge().get("custom-log-appender");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender.install(openTelemetry);
		System.out.println("openTelemetry.getLogsBridge() (a) ||  " + openTelemetry.getLogsBridge());
		// logger.debug("Init Function=> HttpInvokeFn");
		OtelConfig.maybeRunWithSpan(() -> slf4jLogger.info("A slf4j log || Azure Functions with HTTP Trigger"), false);

	}

	/**
	 * This function listens at endpoint "/api/HttpExample". Two ways to invoke it
	 * using "curl" command in bash: 1. curl -d "HTTP Body" {your
	 * host}/api/HttpExample 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
	 */
	@FunctionName("HttpInvokeFn")
	public HttpResponseMessage run(@HttpTrigger(name = "req", methods = { HttpMethod.GET,
			HttpMethod.POST }, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
			final ExecutionContext context) {

		// context.getLogger().info("Java HTTP trigger processed a request.");
		// logger.debug("Java HTTP trigger processed a request.");
		/*
		 * OtelConfig.maybeRunWithSpan(() ->
		 * slf4jLogger.info("A slf4j log || Java HTTP trigger processed a request."),
		 * false);
		 */
		OtelConfig.maybeRunWithSpan(() -> customAppenderLogger.logRecordBuilder().setSeverity(Severity.INFO)
				.setBody("A log message || Azure Functions with HTTP Trigger")
				.setAttribute(AttributeKey.stringKey("azureFn"), "OTEL").emit(), false);

		// Parse query parameter
		final String query = request.getQueryParameters().get("name");
		final String name = request.getBody().orElse(query);

		if (name == null) {
			return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
					.body("Please pass a name on the query string or in the request body").build();
		} else {
			return request.createResponseBuilder(HttpStatus.OK).body("Hello, " + name).build();
		}
	}
}
