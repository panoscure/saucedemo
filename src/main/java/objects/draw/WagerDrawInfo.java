package objects.draw;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class WagerDrawInfo {

    @SerializedName("wagerStartDrawId")
    @Expose
    private Integer wagerStartDrawId;
    @SerializedName("wagerStartDrawDate")
    @Expose
    private String wagerStartDrawDate;
    @SerializedName("wagerEndDrawId")
    @Expose
    private Integer wagerEndDrawId;
    @SerializedName("wagerEndDrawDate")
    @Expose
    private String wagerEndDrawDate;

    public Integer getWagerStartDrawId() {
        return wagerStartDrawId;
    }

    public void setWagerStartDrawId(Integer wagerStartDrawId) {
        this.wagerStartDrawId = wagerStartDrawId;
    }

    public String getWagerStartDrawDate() {
        return wagerStartDrawDate;
    }

    public void setWagerStartDrawDate(String wagerStartDrawDate) {
        this.wagerStartDrawDate = wagerStartDrawDate;
    }

    public Integer getWagerEndDrawId() {
        return wagerEndDrawId;
    }

    public void setWagerEndDrawId(Integer wagerEndDrawId) {
        this.wagerEndDrawId = wagerEndDrawId;
    }

    public String getWagerEndDrawDate() {
        return wagerEndDrawDate;
    }

    public void setWagerEndDrawDate(String wagerEndDrawDate) {
        this.wagerEndDrawDate = wagerEndDrawDate;
    }
}
