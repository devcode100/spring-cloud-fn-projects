package com.todo.main.handler;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.todo.main.dto.StoreDataDto;

/**
 * Azure Functions with HTTP Trigger.
 */
@Component
public class StoreDataHandler {

	@Autowired
	Function<String, List<StoreDataDto>> fetchStoreData;

	@FunctionName("fetchStoreData")
	public HttpResponseMessage execute(@HttpTrigger(name = "request", methods = {
			HttpMethod.GET }, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
			final ExecutionContext context) {
		context.getLogger().info("Java HTTP trigger processed a request.");

		// Parse query parameter
		final String queryCategory = request.getQueryParameters().get("category");
		if (queryCategory == null) {
			return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
					.body("Please pass a valid Category as query param.").build();
		}

		return request.createResponseBuilder(HttpStatus.OK).body(fetchStoreData.apply(queryCategory))
				.header("Content-Type", "application/json").build();
	}
}
