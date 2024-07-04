package model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointsAdjustmentModel {
	@JsonProperty("user_id")
	public String user_id;
	@JsonProperty("descriptor")
	public String descriptor;
	@JsonProperty("amount")
	public int amount;
	@JsonProperty("adjustment_type")
	public String adjustment_type;
	@JsonProperty("reference_id")
	public String reference_id;
	@JsonProperty("reason")
	public String reason;
	@JsonProperty("expiry")
	public String expiry;

	public PointsAdjustmentModel(String user_id, String descriptor, int amount, String adjustment_type,
			String reference_id, String reason, String expiry) {
		this.user_id = user_id;
		this.descriptor = descriptor;
		this.amount = amount;
		this.adjustment_type = adjustment_type;
		this.reference_id = reference_id;
		this.reason = reason;
		this.expiry = expiry;

	}

}
