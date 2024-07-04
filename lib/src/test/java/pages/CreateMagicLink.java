package pages;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.*;
import tests.BaseTest;
import utils.*;
import org.apache.commons.configuration.ConfigurationException;
import org.testng.Assert;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class CreateMagicLink extends BaseTest {

	private String authToken;
	private String userId;
	private String pointsAdjustmentStatus;
	private String magicLink;
	private String firstName;
	private String lastName;
	private String email;
	private String total_points;
	private String points_bal;

	public CreateMagicLink() throws IOException {
		intitconfig(); // Assuming this initializes your test configuration (not shown in the snippet)
	}

	public String getAuthToken() {
		if (authToken == null) {
			String grant_type = prop.getProperty("grant_type");
			String client_id = prop.getProperty("client_id");
			String client_secret = prop.getProperty("client_secret");
			String audience = prop.getProperty("audience");

			TokenModel tokenModel = new TokenModel(grant_type, client_id, client_secret, audience);

			RestAssured.baseURI = prop.getProperty("baseUrl");

			Response response = given().contentType("application/json").body(tokenModel).when().post("oauth/token")
					.then().extract().response();
			if (response.getStatusCode() != 200) {
				List<Map<String, String>> errors = response.jsonPath().getList("errors");
				for (Map<String, String> error : errors) {
					LoggerUtil.log("Error ID: " + error.get("id"));
					LoggerUtil.log("Error Code: " + error.get("code"));
					LoggerUtil.log("Error Message: " + error.get("message"));
				}
			} else {
				JsonPath jsonPath = response.jsonPath();
				authToken = jsonPath.get("token");
				LoggerUtil.log(response.asString()); // Log response for debugging
			}
		}
		return authToken;

	}

	public String createUserId() {
		if (userId == null) {
			RestAssured.baseURI = prop.getProperty("baseUrl");
			Utils utils = new Utils(); // Assuming Utils class provides utility methods (not shown in the snippet)
			utils.genrateRandomUser(); // Assuming this generates random user information
			String fname = utils.getFirstName();
			String lname = utils.getLastName();
			String email = utils.getEmail();
			String phno = "+6584304536";
			String pid = utils.getPartnerUserID();
			System.out.println(fname + "," + lname + "," + email + "," + phno + "," + pid);

			UserModel userinfo = new UserModel(fname, lname, email, "male", "1993-09-07", "SG", phno, pid, "active");

			Response response = given().contentType("application/json")
					.header("Authorization", "Bearer " + getAuthToken()).body(userinfo).when().post("users").then()
					.extract().response();

			if (response.getStatusCode() != 201) {
				List<Map<String, String>> errors = response.jsonPath().getList("errors");
				for (Map<String, String> error : errors) {
					LoggerUtil.log("Error ID: " + error.get("id"));
					LoggerUtil.log("Error Code: " + error.get("code"));
					LoggerUtil.log("Error Message: " + error.get("message"));
					getMagiclink();
				}
			} else {
				JsonPath jsonPath = response.jsonPath();
				userId = jsonPath.get("id");
				LoggerUtil.log("Create User:\n" + response.asString()); // Log response for debugging
			}
		}
		return userId;
	}

	public Response getUserInfo() {
		RestAssured.baseURI = prop.getProperty("baseUrl");
		Response res = given().contentType("application/json").header("Authorization", "Bearer " + getAuthToken())
				.when().get("users/" + createUserId()).then().extract().response();

		if (res.getStatusCode() != 200) {
			List<Map<String, String>> errors = res.jsonPath().getList("errors");
			for (Map<String, String> error : errors) {
				LoggerUtil.log("Error ID: " + error.get("id"));
				LoggerUtil.log("Error Code: " + error.get("code"));
				LoggerUtil.log("Error Message: " + error.get("message"));
			}
		} else {
			JsonPath jsonpath = res.jsonPath();
			LoggerUtil.log("Get User Info:\n" + res.asString());

			// Extract and store first name, last name, and email
			firstName = jsonpath.getString("first_name");
			lastName = jsonpath.getString("last_name");
			email = jsonpath.getString("email");
		}
		return res;
	}

	public Response adjustPoints() {
		Response res = null;
		if (pointsAdjustmentStatus == null) {
			Response getInfoRes = getUserInfo();
			JsonPath jsonpath = getInfoRes.jsonPath();
			String userid = jsonpath.get("id");
			String partner_id = jsonpath.get("partner_user_id");
			RestAssured.baseURI = prop.getProperty("baseUrl");
			System.out.println(userid + "," + partner_id);

			PointsAdjustmentModel adjustPoints = new PointsAdjustmentModel(userid, "add points", 20000, "campaign",
					partner_id, "new sign up", null);

			res = given().contentType("application/json").header("Authorization", "Bearer " + getAuthToken())
					.body(adjustPoints).when().post("point_adjustments").then().extract()
					.response();
			// Check for errors in the response
			if (res.getStatusCode() != 201) {
				List<Map<String, String>> errors = res.jsonPath().getList("errors");
				for (Map<String, String> error : errors) {
					LoggerUtil.log("Error ID: " + error.get("id"));
					LoggerUtil.log("Error Code: " + error.get("code"));
					LoggerUtil.log("Error Message: " + error.get("message"));
					getMagiclink();
				}
			} else {
				JsonPath jsonobj = res.jsonPath();
				LoggerUtil.log("Adjust Points:\n" + res.asString());
				pointsAdjustmentStatus = jsonobj.getString("status");
				total_points = jsonobj.getString("amount");

				Assert.assertTrue(pointsAdjustmentStatus.contains("confirmed"));
			}
		}

		return res;
	}

	public String getMagiclink() {
		if (magicLink == null) {
			Response getPointsRes = adjustPoints();
			JsonPath jsonpath = getPointsRes.jsonPath();
			String userid = jsonpath.get("user_id");
			Utils utils = new Utils();
//			String srefid = utils.getPartnerUserID();
//			String[] refids = srefid.split("-");
//			StringBuffer sb = new StringBuffer();
//			for (int i = 0; i < refids.length; i++) {
//				String word = refids[i];
//				sb.append(word);
//			}
			RestAssured.baseURI = prop.getProperty("baseUrl");
			MagicLinkModel sessionid = new MagicLinkModel(Utils.SeqNumber());
			Response res = given().contentType("application/json").header("Authorization", "Bearer " + getAuthToken())
					.body(sessionid).when().post("users/" + userid + "/login_link").then().extract().response();
			if (res.getStatusCode() != 200) {
				List<Map<String, String>> errors = res.jsonPath().getList("errors");
				for (Map<String, String> error : errors) {
					LoggerUtil.log("Error ID: " + error.get("id"));
					LoggerUtil.log("Error Code: " + error.get("code"));
					LoggerUtil.log("Error Message: " + error.get("message"));
				}
			} else {
				JsonPath jsonobj = res.jsonPath();
				LoggerUtil.log(res.asString());
				magicLink = jsonobj.getString("login_link");
			}
		}
		return magicLink;
	}

	public Response getUserInfo(String userid) {
		RestAssured.baseURI = prop.getProperty("baseUrl");
		Response res = given().contentType("application/json").header("Authorization", "Bearer " + getAuthToken())
				.when().get("users/" + userid).then().extract().response();

		if (res.getStatusCode() != 200) {
			List<Map<String, String>> errors = res.jsonPath().getList("errors");
			for (Map<String, String> error : errors) {
				LoggerUtil.log("Error ID: " + error.get("id"));
				LoggerUtil.log("Error Code: " + error.get("code"));
				LoggerUtil.log("Error Message: " + error.get("message"));
			}
		} else {
			JsonPath jsonpath = res.jsonPath();
			LoggerUtil.log("Get User Info:\n" + res.asString());
		}

		return res;
	}

	// Getter methods to access stored values
	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getTotalAmount() {
		return total_points;
	}

	public String getUserId() {
		return userId;
	}

	public String getMagicLink() {
		return magicLink;
	}
}
