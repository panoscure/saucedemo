package objects.draw;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class DrawInfoObject {

    @SerializedName("drawId")
    @Expose
    private Integer drawId;
    @SerializedName("drawDate")
    @Expose
    private BigInteger drawDate;

    public Integer getDrawId() {
        return drawId;
    }

    public void setDrawId(Integer drawId) {
        this.drawId = drawId;
    }

    public BigInteger getDrawDate() {
        return drawDate;
    }

    public void setDrawDate(BigInteger drawDate) {
        this.drawDate = drawDate;
    }
}
