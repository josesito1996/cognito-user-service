package com.samy.service.cognitoapp.service;

import com.google.gson.JsonObject;

public interface LambdaService {

    public JsonObject validEmailWithLambda(String payLoad);
    
    
    public JsonObject validEmailAndRegisterWithLambda(String payLoad);
    
    
    public JsonObject mailSendWithLambda(String payLoad);
    
}
