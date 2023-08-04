
package models.apigatewayj.myprofileservice.getmyfavoriteplayslips;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Panel {

    @SerializedName("options")
    @Expose
    private List<Object> options;
    @SerializedName("quickPick")
    @Expose
    private Boolean quickPick;
    @SerializedName("requested")
    @Expose
    private Integer requested;
    @SerializedName("selection")
    @Expose
    private List<Integer> selection;

    public List<Object> getOptions() {
        return options;
    }

    public void setOptions(List<Object> options) {
        this.options = options;
    }

    public Boolean getQuickPick() {
        return quickPick;
    }

    public void setQuickPick(Boolean quickPick) {
        this.quickPick = quickPick;
    }

    public Integer getRequested() {
        return requested;
    }

    public void setRequested(Integer requested) {
        this.requested = requested;
    }

    public List<Integer> getSelection() {
        return selection;
    }

    public void setSelection(List<Integer> selection) {
        this.selection = selection;
    }

}
