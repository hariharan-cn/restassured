package com.sampletest.test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.simple.parser.JSONParser;
import org.testng.annotations.Test;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

/**
 * Created by Administrator on 08-05-2017.
 */
public class testone {
    String logtoken;
    String logtoken2;


    endPoint ep = new endPoint();
    CommonWrapper common = new CommonWrapper("datasource/teams.json");


    public testone() throws IOException, ParseException {
    }


    @Test(enabled = false)
    public void validate(){
        //given().get("https://cu036.cloud.maa.collab.net/apidoc/swagger/#!/Tags/getTag").then().statusCode(200).log().all();
        given().get("https://cu036.cloud.maa.collab.net/ctfrest/tags/v1/tags/tag1001").then().statusCode(403).log().all();

    }

    @Test
    public void postExample()
    {
        String myJson = "{\"username\":\"admin\",\"password\":\"admin\",\"grant_type\":\"password\",\"client_id\":\"api-client\",\"scope\":\"urn:ctf:services:ctf urn:ctf:services:svn urn:ctf:services:gerrit urn:ctf:services:soap60\"}";
        Map<String,String> car = new HashMap<String,String>();
        car.put("username", "admin");
        car.put("password", "admin");
        car.put("grant_type", "password");
        car.put("client_id", "api-client");
        car.put("scope", "urn:ctf:services:ctf urn:ctf:services:svn urn:ctf:services:gerrit urn:ctf:services:soap60");
        RestAssured.baseURI  = "https://cu036.cloud.maa.collab.net/sf/auth/token";


        System.out.println("dummy1"+car);
        Response response = given()
                .params(car).
                        when().
                        post("https://cu036.cloud.maa.collab.net/sf/auth/token").then().
            extract().
            response();

        String body = response.getBody().asString();
        logtoken = response.path("access_token");

        System.out.println("DUMMY 2"+body);
        System.out.println("DUMMY 3"+logtoken);

    }

    @Test (enabled = false)
    public void createTag()
    {


        Map<String, Object>  jsonAsMap = new HashMap<String, Object>();
        jsonAsMap.put("description", "aaaaabbb");
        jsonAsMap.put("name", "aaaaabbb");


        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer " + logtoken);

        given().headers(headers).
        contentType("application/json").
                body(jsonAsMap)
                .when().post("https://cu036.cloud.maa.collab.net/ctfrest/tags/v1/projects/proj1010/tags").then()
                .statusCode(201);

        System.out.println(logtoken);
    }

    @Test (enabled = false)
    public void getTagID()
    {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer " + logtoken);


        String jsony = given().headers(headers).
                contentType("application/json")
                .when().get("https://cu036.cloud.maa.collab.net/ctfrest/tags/v1/projects/proj1010/tags").asString();
        JsonPath jp = new JsonPath(jsony);
         jp.setRoot("items");
        System.out.println(jsony);


        Map items = jp.get("find {i -> i.name == 'khhk' }");
        //Map items = jp.get("find {e -> e.name =~ /khhk/}");
        assertEquals("user1003", items.get("createdById"));
        assertEquals("khhk", items.get("description"));
        assertEquals("proj1010", items.get("projectId"));
    }

    @Test (dependsOnMethods = "postExample")
    public void createTag2() throws ParseException,IOException {

        /*
        Map<String, Object>  jsonAsMap = new HashMap<String, Object>();
        jsonAsMap.put("description", "aaaaabbb");
        jsonAsMap.put("name", "aaaaabbb"); */

        JSONParser parser = new JSONParser();
        File file = new File("E:\\datasource\\test.json");
        Object obj = parser.parse(new FileReader(file));


        JSONObject jsonObject = (JSONObject) obj;
        System.out.println(jsonObject);

        String jsony = jsonObject.toJSONString();

        //System.out.println("test:"+jsony.toString());

        JsonPath jp = new JsonPath(jsony);


        jp.setRoot("login");
        ArrayList<String> Create_AlmDevops = jp.get("username");
        Object tester = Create_AlmDevops.get(0);
        System.out.println(tester);

    }

    @Test//(dependsOnMethods = "postExample")
    public void passJson() throws IOException, ParseException {

        JSONParser parser = new JSONParser();
        File file = new File("E:\\datasource\\test.json");
        Object obj = parser.parse(new FileReader(file));
        JSONObject jsonObject = (JSONObject) obj;
        System.out.println("PRINT1"+jsonObject.get("login"));


        JSONArray abcd = (JSONArray) jsonObject.get("login");

        System.out.println("PRINT 2" + abcd.get(0));

        Response response = given()
                .params((Map<String, ?>) abcd.get(0)).
                        when().
                        post("https://cu036.cloud.maa.collab.net/sf/auth/token").then().
                        extract().
                        response();

        String body = response.getBody().asString();
        logtoken = response.path("access_token");

       // System.out.println(body);
        //System.out.println("##############"+response);

    }

    @Test (dependsOnMethods = "postExample")
    public void createTagFinal() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        File file = new File("E:\\datasource\\test.json");
        Object obj = parser.parse(new FileReader(file));
        JSONObject jsonObject = (JSONObject) obj;

        JSONArray abcd = (JSONArray) jsonObject.get("Create_Tag1");

        System.out.println("PRINT 2" + abcd.get(0));

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer " + logtoken);

        given().headers(headers).
                contentType("application/json").
                body(abcd.get(0))
                .when().post("https://cu036.cloud.maa.collab.net/ctfrest/tags/v1/projects/proj1010/tags").then()
                .statusCode(201);

        System.out.println(ep.getListOfTagsForProject);

    }


    @Test (dependsOnMethods = "postExample")
    public void EditTags() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        File file = new File("E:\\datasource\\test.json");
        Object obj = parser.parse(new FileReader(file));
        JSONObject jsonObject = (JSONObject) obj;

        JSONArray abcd = (JSONArray) jsonObject.get("Edit_Tags");

        System.out.println("PRINT 2" + abcd.get(0));

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer " + logtoken);
        headers.put("If-Match", "*");

        given().headers(headers).
                contentType("application/json").
                body(abcd.get(0))
                .when().patch("https://cu036.cloud.maa.collab.net/ctfrest/tags/v1/tags/tag1001").then()
                .statusCode(200);

        System.out.println(ep.getListOfTagsForProject);

    }


    @Test
    public void projectTags() throws IOException, ParseException {
    String URL = "https://cu036.cloud.maa.collab.net/ctfrest/tags/v1/tags/tag1001";
    //common.getCallVerifyCode(URL,200);
    }

    @Test
    public void postTags() throws IOException, ParseException {
        String URL = "https://cu036.cloud.maa.collab.net/ctfrest/tags/v1/projects/proj1010/tags";
       // common.postCallVerifyCode(URL,"Create_Tag1",201);



    }

    @Test
    public void assertTags() throws IOException, ParseException {
        String URL = "https://cu036.cloud.maa.collab.net/ctfrest/tags/v1/projects/proj1010/tags";
      /*  Map items = common.GetCall(URL,"name","HaPpY3");

        assertEquals("user1003", items.get("createdById"));
        assertEquals("HaPpY3", items.get("description"));
        assertEquals("proj1010", items.get("projectId")); */
    }

    @Test
    public void globalValues() throws IOException, ParseException {
        ep.Messi="AAAA";


    }

    @Test (dependsOnMethods = "globalValues")
    public void printGlobalValues() throws IOException, ParseException {
        System.out.println(ep.Messi);


    }





}
