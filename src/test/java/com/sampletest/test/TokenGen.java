package com.sampletest.test;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Administrator on 22-05-2017.
 */
public class TokenGen {
    CommonWrapper common = new CommonWrapper("datasource/teams.json");
    Prop tk = new Prop();
    String AdminToken;
    JSONParser parser = new JSONParser();
    File file = new File("E:\\datasource\\test.json");
    Object obj = parser.parse(new FileReader(file));
    JSONObject jsonObject = (JSONObject) obj;

    public TokenGen() throws IOException, ParseException {
    }

    @Test
    public void adminTokenGen() throws IOException, ParseException {
      // token.Admintoken = common.getLoginAcessToken("AdminLogin");
        AdminToken = common.getLoginAcessToken("AdminLogin");
        AdminToken = tk.Admintoken;
       //return AdminToken;
        System.out.println("Running first");
    }

    @Test
    public void fURL()  {
        //String URL = partialURL+"/tags/tag1001";
        //System.out.println(URL);
        System.out.println("Running 2nd");
        System.out.println(AdminToken);
    }
}
