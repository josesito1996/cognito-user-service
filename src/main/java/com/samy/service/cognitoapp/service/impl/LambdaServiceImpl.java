package com.samy.service.cognitoapp.service.impl;

import static com.samy.service.cognitoapp.utils.Constant.lambdaMailSenderNombre;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.samy.service.cognitoapp.service.LambdaService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LambdaServiceImpl implements LambdaService {

	@Autowired
	private AWSLambda lambdaService;

	private String lambdaValidatorName = "email-validator-lambda";

	private String lambdaValidatorNameAndRegister = "lambda-mail-service";

	@Override
	public JsonObject validEmailWithLambda(String payLoad) {
		log.info("LambdaServiceImpl.validEmailWithLambda");
		InvokeRequest invokeRequest = new InvokeRequest()
				.withFunctionName(lambdaValidatorName)
				.withPayload(payLoad);
		InvokeResult result = lambdaService.invoke(invokeRequest);
		String ans = new String(result.getPayload().array(), StandardCharsets.UTF_8);
		JsonElement element = JsonParser.parseString(ans);
		return element.getAsJsonObject();
	}

	@Override
	public JsonObject mailSendWithLambda(String payLoad) {
		log.info("LambdaServiceImpl.mailSendWithLambda");
		InvokeRequest invokeRequest = new InvokeRequest()
				.withFunctionName(lambdaMailSenderNombre)
				.withPayload(payLoad);
		InvokeResult result = lambdaService.invoke(invokeRequest);
		String ans = new String(result.getPayload().array(), StandardCharsets.UTF_8);
		JsonElement element = JsonParser.parseString(ans);
		return element.getAsJsonObject();
	}

	@Override
	public JsonObject validEmailAndRegisterWithLambda(String payLoad) {
		log.info("LambdaServiceImpl.validEmailAndRegisterWithLambda");
		log.info("PayLoad : " + payLoad);
		InvokeRequest invokeRequest = new InvokeRequest()
				.withFunctionName(lambdaValidatorNameAndRegister)
				.withPayload(payLoad);
		InvokeResult result = lambdaService.invoke(invokeRequest);
		String ans = new String(result.getPayload().array(), StandardCharsets.UTF_8);
		log.info("Result : " + ans);
		JsonElement element = JsonParser.parseString(ans);
		return element.getAsJsonObject();
	}

}
