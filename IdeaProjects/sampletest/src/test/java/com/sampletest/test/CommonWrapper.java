package com.sampletest.test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.entity.ContentType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.util.Properties;
import java.util.*;
import static io.restassured.RestAssured.given;


public class CommonWrapper {

    private String tokenType = null;
    private String partialURL = null;


    //Configuration
    private String hostName = null;
    String tagsBaseURL,foundationBaseURL;
    private JSONParser parser = null;
    InputStream jsonConfigFile = null;
    JSONObject jsonObject = null;
    Properties properties = new Properties();

    public void loadPropFile() throws IOException, ParseException {
        File file = new File(CommonConstants.CONFIG_PROP_FILE);
        FileInputStream fileInput = new FileInputStream(file);
        properties.load(fileInput);
        hostName=properties.getProperty("hostname");
        tagsBaseURL= properties.getProperty("baseURL_Tags");
    }

    public String getHostName() {
        return hostName;
    }

    public String getTagsBaseURL(){
        return tagsBaseURL;
    }

    public String getFoundationBaseURL(){
        return foundationBaseURL;
    }
    private void loadConfigFile(String configFile) throws IOException, ParseException {
        parser = new JSONParser();
        jsonConfigFile = this.getClass().getClassLoader().getResourceAsStream(configFile);
        Object obj = parser.parse(new InputStreamReader(jsonConfigFile));
        jsonObject = (JSONObject) obj;
    }

    public CommonWrapper(String configFile) throws IOException, ParseException {
        loadConfigFile(configFile);
        loadPropFile();
    }

    public String getLoginAcessToken(String setValue) throws IOException, ParseException {
        JSONArray jsonArray = (JSONArray) jsonObject.get(setValue);

        Response response = given()
                .params((Map<String, ?>) jsonArray.get(0)).
                        when().
                        post(hostName +"/sf/auth/token").then().
                        extract().
                        response();

        String body = response.getBody().asString();
        String token = response.path("access_token");
        tokenType = response.path("token_type");

        return token;
    }

    public String constructPartialURL(String baseURL){
        partialURL = this.hostName +baseURL;
        return partialURL;
    }

    public void getAndVerifyCode(String token,String URL, int SCode) throws IOException, ParseException {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(CommonConstants.AUTHORIZATION_HEADER, "Bearer " + token);

        given().headers(headers).
                contentType("application/json")
                .when().get(URL).then().log().all().
                statusCode(SCode);
    }

    public Map getCall(String token, String URL, String name, String value) throws IOException, ParseException {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(CommonConstants.AUTHORIZATION_HEADER, "Bearer " + token);

        String jsony = given().headers(headers).
                contentType("application/json")
                .when().get(URL).asString();
        JsonPath jp = new JsonPath(jsony);
        jp.setRoot("items");
        Map items = jp.get("find {i -> i."+name+" == '"+value+"' }");

        return items;
    }



    public void postCallVerifyCode(String token,String URL, String setValue,int SCode) throws IOException, ParseException {

        JSONArray abcd = (JSONArray) jsonObject.get(setValue);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer " + token);

        given().headers(headers).
                contentType("application/json").
                body(abcd.get(0))
                .when().post(URL).then().extract();
    }

    public Object postCall(String token, String URL, String setValue) throws IOException, ParseException {
        JSONArray jsonArray = (JSONArray) jsonObject.get(setValue);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put(CommonConstants.AUTHORIZATION_HEADER, "Bearer " + token);
        headers.put("Content-Type", ContentType.APPLICATION_JSON.getMimeType());

        Response response = given().headers(headers).
                body(jsonArray.get(0)).
                when().
                post(URL).then()
                .extract().
                        response();

        String bodystring=response.getBody().asString();
        JsonPath jsonPath =JsonPath.from(bodystring);

        return jsonPath;
    }

    public void patchCallVerifyCode(String token,String URL, String setValue, int SCode) throws IOException, ParseException {

        JSONArray abcd = (JSONArray) jsonObject.get(setValue);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer " + token);
        headers.put("If-Match", "*");

        given().headers(headers).
                contentType("application/json").
                body(abcd.get(0))
                .when().patch(URL).then()
                .statusCode(SCode);
    }

    public String patchCall(String token,String URL, String setValue) throws IOException, ParseException {

        JSONArray jsonArray = (JSONArray) jsonObject.get(setValue);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put(CommonConstants.AUTHORIZATION_HEADER, "Bearer " + token);
        headers.put("If-Match", "*");

        Response response = given().headers(headers).
                contentType(ContentType.APPLICATION_JSON.getMimeType()).
                body(jsonArray.get(0))
                .when().patch(URL).then().
                        extract().
                        response();

        String body = response.getBody().asString();
        return body;
    }

    public String readJsonValue(String attrName,String ParaName) throws IOException, ParseException {

        String jsony = jsonObject.toJSONString();
        JsonPath jp = new JsonPath(jsony);
        jp.setRoot(attrName);
        ArrayList<String> Json_Value_Array = jp.get(ParaName);
        String Jvalue = Json_Value_Array.get(0).toString();
        System.out.println(Jvalue);
        return Jvalue;
    }

    public void deleteCallVerifyCode(String token,String URL, int SCode) throws IOException, ParseException {

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer " + token);

        given().headers(headers).contentType("application/json").delete(URL).then().statusCode(SCode);

    }

    public String readResponseValue(String attrName) throws IOException, ParseException {

        String jsony = jsonObject.toJSONString();
        JsonPath jp = new JsonPath(jsony);
        jp.getJsonObject(attrName);
        String Jvalue = jp.getJsonObject(attrName).toString();
        System.out.println(Jvalue);
        return Jvalue;
    }






}
