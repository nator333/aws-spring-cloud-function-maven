package com.serverless.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import java.util.Collections;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ByeHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = LogManager.getLogger(ByeHandler.class);
	private static final Map RESPONCE_MAP = Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless");

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

		LOG.info("received: ");
    input.forEach((a, b)->{
      if (b != null) {
        LOG.info("    key : {}, value : {}", a, b);
      }
    });

		Response responseBody =
        new Response("Bye Serverless v1.x! Your function executed successfully!", input);

		return ApiGatewayResponse.builder()
				.setStatusCode(200)
				.setObjectBody(responseBody)
				.setHeaders(RESPONCE_MAP)
				.build();
	}
}
