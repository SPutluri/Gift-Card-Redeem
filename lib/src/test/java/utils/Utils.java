package utils;

import com.github.javafaker.DateAndTime;
import com.github.javafaker.Faker;
import model.UserModel;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class Utils {

    public String first_name;
    public String last_name;
    public String email;
    public String partner_id;
    public Date dob;
    public String phn;

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public String getPartnerUserID() {
        return partner_id;
    }

    public String getDob() {
        return dob.toString();
    }
    public String getEmail() {
        return email;
    }
    public String getPhn() {
        return phn;
    }


    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }
    public void setLastName(String last_name) {this.last_name = last_name;}
    public void setPartnerUserID(String partner_id) {this.partner_id = partner_id;}
    public void setDob(Date dob) {this.dob = dob;}


    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhn(String phn) {
        this.phn = phn;
    }

    public void genrateRandomUser() {
        Faker faker = new Faker();
        setFirstName(faker.name().firstName());
        setLastName(faker.name().lastName());
        setEmail(faker.internet().emailAddress());
        setPartnerUserID(faker.internet().uuid());
        setDob(faker.date().birthday());
        setPhn("+" + faker.phoneNumber().phoneNumber());

    }

    public static String randomNumber() {
        String prefix = "+65";
        int min = 80000000;
        int max = 99999999;
        int phoneNumber = (int) Math.round(Math.random() * (max - min) + min);
        return prefix + phoneNumber;
    }

//    public static void main(String[] args) throws IOException, ParseException {
//        //System.out.println(readJSONFile("./src/test/resources/extrainfo.json", 2,get("createdUser2Role")));
////     String token= (String) ;
//        Utils utils = new Utils();
////        UserModel testUser = new UserModel("jjjj", "badhon", "1234", "01771", "1997", "Customer");
////        System.out.println(testUser);
//    }

    public static int generateRandomId(int min, int max) {
        double number = Math.random() * (max - min) + min;
        return (int) number;
    }
    
    public static String SeqNumber() {
       String prefix = "seqid";
        int min = 100000000;
        int max = 999999999;
        int randumber = (int) Math.round(Math.random() * (max - min) + min);
        return prefix+randumber;
    }





}