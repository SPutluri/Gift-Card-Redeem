package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserModel {
	@JsonProperty("email")
	public String email;
	@JsonProperty("first_name")
	public String first_name;
	@JsonProperty("last_name")
	private String last_name;
	@JsonProperty("phone_number")
	private String phone_number;
	@JsonProperty("birthdate")
	private String birthdate;
	@JsonProperty("status")
	private String status;
	@JsonProperty("gender")
	private String gender;
	@JsonProperty("partner_user_id")
	private String partner_user_id;
	@JsonProperty("country_code")
	private String country_code;
//	@JsonProperty("seq_id")
//	private String seq_id;

	public UserModel() {
		// Default constructor
	}

	public UserModel(String first_name, String last_name, String email, String gender, String birthdate,
			String country_code, String phone_number, String partner_user_id, String status) {
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.gender = gender;
		this.birthdate = birthdate;
		this.country_code = country_code;
		this.phone_number = phone_number;
		this.partner_user_id = partner_user_id;
		this.status = status;
	}

//	public UserModel(String seq_id) {
//		this.seq_id = seq_id;
//	}
}
