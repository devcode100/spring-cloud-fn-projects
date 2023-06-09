package com.app.main.handler;

import java.util.List;
import java.util.Optional;

import org.springframework.cloud.function.adapter.azure.FunctionInvoker;

import com.app.main.dto.CosmosTestDataDto;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

/**
 * Azure Functions with HTTP Trigger.
 */
public class CosmosTestDataHandler extends FunctionInvoker<String, List<CosmosTestDataDto>> {
	/**
	 * This function listens at endpoint "/api/spring-cosmos-nosql-fn-http-trigger".
	 * Two ways to invoke it using "curl" command in bash: 1. curl -d "HTTP Body"
	 * {your host}/api/spring-cosmos-nosql-fn-http-trigger 2. curl "{your
	 * host}/api/spring-cosmos-nosql-fn-http-trigger?name=HTTP%20Query"
	 */
	@FunctionName("getCosmosTestData")
	public HttpResponseMessage execute(@HttpTrigger(name = "req", methods = {
			HttpMethod.GET }, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
			final ExecutionContext context) {
		context.getLogger().info("Java HTTP trigger processed a request.");

		// Parse query parameter
		final String queryCategory = request.getQueryParameters().get("category");
		if (queryCategory == null) {
			return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
					.body("Please pass a valid Category as query param.").build();
		}

		return request.createResponseBuilder(HttpStatus.OK).body(handleRequest(queryCategory, context))
				.header("Content-Type", "application/json").build();
	}
}
