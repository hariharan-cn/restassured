package com.sampletest.test;

import io.restassured.path.json.JsonPath;
import org.json.simple.parser.ParseException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.Map;
import static org.testng.AssertJUnit.assertEquals;


public class Tags {
    CommonWrapper calls = new CommonWrapper("datasource/tags.json");
    String baseURLTags= calls.getTagsBaseURL();
    String baseURLFoundation= calls.getFoundationBaseURL();
    String parURL = calls.getHostName()+baseURLTags;
    String ProjectID = "proj1010";
    String URL,TagID;
    String Admintoken, RestrictedUsertoken;

    public Tags() throws IOException, ParseException {
    }

    @BeforeClass
    public void preRequ() throws IOException, ParseException {
        Admintoken = calls.getLoginAcessToken("AdminLogin");
        RestrictedUsertoken = calls.getLoginAcessToken("RestrictedUserLogin");
    }



    @Test //(enabled = false)
    public void getAdminToken() throws IOException, ParseException {
        Admintoken = calls.getLoginAcessToken("AdminLogin");
    }

    public void getRestrictedUserToken() throws IOException, ParseException {
        RestrictedUsertoken = calls.getLoginAcessToken("RestrictedUserLogin");
    }


    @Test(description = "Admin User : Creating Tag and Verifying the Tag description")
    public void createTag() throws IOException, ParseException {

        String URL = parURL+"/projects/"+ProjectID+"/tags";
        String compValue = calls.readJsonValue("Create_Tag","description");

        JsonPath jsonPath = (JsonPath) calls.postCall(Admintoken,URL,"Create_Tag");
        assertEquals(compValue, jsonPath.get("description"));
        TagID = jsonPath.getString("id");


    }


    @Test (description = "Admin User: Create Tag and verify its Status Code ")
    public void verifyCreateTagCode() throws IOException, ParseException {
        String URL = parURL+"/projects/"+ProjectID+"/tags";
        calls.postCallVerifyCode(Admintoken,URL,"Create_Tag2",201);
    }

    @Test(description = "Get List of Tags in Project and Verify their UserID", dependsOnMethods = "getAdminToken" )
    public void getCreatedTag() throws IOException, ParseException {

        String URL = parURL+"/projects/"+ProjectID+"/tags";
        Map items = calls.getCall(Admintoken,URL,"name","system7");
        assertEquals("user1003", items.get("createdById"));
        assertEquals("Delhi100", items.get("description"));
        assertEquals("proj1010", items.get("projectId"));
    }

    @Test(description = "NEG: Get List of Tags in Project - expecting permission denied")
    public void getProjectTags403() throws IOException, ParseException {
        String URL = parURL+"/projects/"+ProjectID+"/tags";
        calls.getAndVerifyCode(RestrictedUsertoken,URL,403);

    }

    @Test(description = "NEG: Get List of Tags in Project - expecting Invalid project id")
    public void getProjectTags404() throws IOException, ParseException {
        ProjectID = "proj343232342";
        String URL = parURL+"/projects/"+ProjectID+"/tags";
        calls.getAndVerifyCode(Admintoken,URL,404);
    }

    @Test(description = "NEG: Create Tags in Project - expecting permission denied")
    public void postCreateTags404() throws IOException, ParseException {
        String URL = parURL+"/projects/"+ProjectID+"/tags";
        calls.postCallVerifyCode(RestrictedUsertoken,URL,"Dummy_Tag",403);
    }

    @Test(description = "NEG: Create Tags in Project - expecting Tag Name conflict")
    public void postCreateTags409() throws IOException, ParseException {
        String URL = parURL+"/projects/"+ProjectID+"/tags";
        calls.postCallVerifyCode(Admintoken,URL,"Create_Tag2",409);
    }

    @Test(description = "Edit Tags names", dependsOnMethods = "createTag")
    public void patchEditTagName() throws IOException, ParseException {
        String URL = parURL+"/tags/"+TagID;
        calls.patchCall(Admintoken,URL,"Edit_Tags");

    }

    @Test(description = "NEG: Edit Tags names - expecting Permission denied",dependsOnMethods = "createTag")
    public void patchEditTagName403() throws IOException, ParseException {
        String URL = parURL+"/tags/"+TagID;
        calls.patchCallVerifyCode(RestrictedUsertoken,URL,"Dummy_Tag",403);
    }
    @Test(description = "NEG: Edit Tags names - expecting Invalid Tag id",dependsOnMethods = "createTag")
    public void patchEditTagName404() throws IOException, ParseException {
        TagID = "tag12323232";
        String URL = parURL+"/tags/"+TagID;
        calls.patchCallVerifyCode(Admintoken,URL,"Dummy_Tag",404);
    }

    @Test(description = "Admin User : Creating Tag and Verifying the Tag description")
    public void deleteTag() throws IOException, ParseException {

        String URL = parURL+"/projects/"+ProjectID+"/tags";
        String compValue = calls.readJsonValue("Create_Tag3","description");
        JsonPath jsonPath = (JsonPath) calls.postCall(Admintoken,URL,"Create_Tag3");
        assertEquals(compValue, jsonPath.get("description"));
        TagID = jsonPath.getString("id");

        String URL2 = parURL+"/tags/"+TagID;
        calls.deleteCallVerifyCode(Admintoken,URL2,204);
    }

    @Test(enabled = false)
    public void demo() throws IOException, ParseException {
        calls.getAndVerifyCode(Admintoken,URL,201);
    }
}
