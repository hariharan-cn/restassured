package com.sampletest.test;

import io.restassured.path.json.JsonPath;
import org.json.simple.parser.ParseException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

import static org.testng.AssertJUnit.assertEquals;

/**
 * Created by Administrator on 24-05-2017.
 */
public class Teams {
    CommonWrapper calls = new CommonWrapper("datasource/teams.json");

    String baseURL= "/ctfrest/team/v1";
    String parURL = calls.getHostName()+baseURL;
    String ProjectID = "proj1010";
    String URL,TeamID;
    String Admintoken, RestrictedUsertoken;

    public Teams() throws IOException, ParseException {
    }


    @BeforeClass
    public void setupURL() throws IOException, ParseException {
        Admintoken = calls.getLoginAcessToken("AdminLogin");
        RestrictedUsertoken = calls.getLoginAcessToken("RestrictedUserLogin");
    }

    @Test(description = "Admin User : Creating Team and Verifying the Team description",enabled = false)
    public void createTeam() throws IOException, ParseException {

        String URL = parURL+"/projects/"+ProjectID+"/teams";
        String compValue = calls.readJsonValue("Create_Team","description");

        JsonPath jsonPath = (JsonPath) calls.postCall(Admintoken,URL,"Create_Team");
        assertEquals(compValue, jsonPath.get("description"));
        TeamID = jsonPath.getString("teamId");
       // calls.getAndVerifyCode(Admintoken,URL,200);

    }


    @Test (description = "Admin User: Create Tag and verify its Status Code ",dependsOnMethods = "createTeam",enabled = false)
    public void getAndVerifyCreatedTeam() throws IOException, ParseException {

        String URL = parURL+"/projects/"+ProjectID+"/teams";
        String compValue = calls.readJsonValue("Create_Team","title");
        Map items = calls.getCall(Admintoken,URL,"title","team A");
        assertEquals(ProjectID, items.get("projectId"));

    }
    @Test
    public void sample() throws IOException, ParseException {
        String URL = parURL+"/projects/"+ProjectID+"/teams";
        calls.postCall(Admintoken,URL,"Create_Team");
    }

}
