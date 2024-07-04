package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MagicLinkModel {


    @JsonProperty("seqid")
    private String seqid;

    public MagicLinkModel() {

    }

    public MagicLinkModel(String seqid) {
        this.seqid = seqid;
        
    }
}
