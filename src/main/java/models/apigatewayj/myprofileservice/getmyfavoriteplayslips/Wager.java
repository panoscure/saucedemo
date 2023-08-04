
package models.apigatewayj.myprofileservice.getmyfavoriteplayslips;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Wager {

    @SerializedName("dbg")
    @Expose
    private List<Dbg> dbg;
    @SerializedName("wagerCost")
    @Expose
    private Double wagerCost;

    public List<Dbg> getDbg() {
        return dbg;
    }

    public void setDbg(List<Dbg> dbg) {
        this.dbg = dbg;
    }

    public Double getWagerCost() {
        return wagerCost;
    }

    public void setWagerCost(Double wagerCost) {
        this.wagerCost = wagerCost;
    }

}
