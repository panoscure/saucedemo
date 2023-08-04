
package models.apigatewayj.myprofileservice.getmyfavoriteplayslips;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class ParticipatingDraws {

    @SerializedName("dayPattern")
    @Expose
    private List<Object> dayPattern;
    @SerializedName("drawOffsets")
    @Expose
    private List<Integer> drawOffsets;
    @SerializedName("multipleDraws")
    @Expose
    private Integer multipleDraws;

    public List<Object> getDayPattern() {
        return dayPattern;
    }

    public void setDayPattern(List<Object> dayPattern) {
        this.dayPattern = dayPattern;
    }

    public List<Integer> getDrawOffsets() {
        return drawOffsets;
    }

    public void setDrawOffsets(List<Integer> drawOffsets) {
        this.drawOffsets = drawOffsets;
    }

    public Integer getMultipleDraws() {
        return multipleDraws;
    }

    public void setMultipleDraws(Integer multipleDraws) {
        this.multipleDraws = multipleDraws;
    }

}
