package com.sampletest.test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;


public class endPoint {
    String logtoken;
    //Configuration
    String hostname="https://cu036.cloud.maa.collab.net";
    String TagbaseURL=" /ctfrest/tags/v1";
    //String Endpoints;
    String URL;

    //
    String projectid;
    String getArtifact = "/artifacts";
    String getListOfTagsForProject = "/projects/"+projectid+"/tags";

    String Messi;


     public String URLConstr(String Endpoints){
        URL = hostname+TagbaseURL+Endpoints;
        return URL;
    }

    public void GetCall(String URL,int StatusCode){

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer " + logtoken);


        String jsony = given().headers(headers).
                contentType("application/json")
                .when().get("https://cu036.cloud.maa.collab.net/ctfrest/tags/v1/projects/proj1010/tags").asString();

    }
}
