
package models.apigatewayj.myprofileservice.getmyfavoriteplayslips;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Board {

    @SerializedName("betType")
    @Expose
    private Integer betType;
    @SerializedName("boardId")
    @Expose
    private Integer boardId;
    @SerializedName("multipliers")
    @Expose
    private List<Integer> multipliers;
    @SerializedName("options")
    @Expose
    private List<Object> options;
    @SerializedName("panels")
    @Expose
    private List<Panel> panels;
    @SerializedName("quickPick")
    @Expose
    private Boolean quickPick;

    public Integer getBetType() {
        return betType;
    }

    public void setBetType(Integer betType) {
        this.betType = betType;
    }

    public Integer getBoardId() {
        return boardId;
    }

    public void setBoardId(Integer boardId) {
        this.boardId = boardId;
    }

    public List<Integer> getMultipliers() {
        return multipliers;
    }

    public void setMultipliers(List<Integer> multipliers) {
        this.multipliers = multipliers;
    }

    public List<Object> getOptions() {
        return options;
    }

    public void setOptions(List<Object> options) {
        this.options = options;
    }

    public List<Panel> getPanels() {
        return panels;
    }

    public void setPanels(List<Panel> panels) {
        this.panels = panels;
    }

    public Boolean getQuickPick() {
        return quickPick;
    }

    public void setQuickPick(Boolean quickPick) {
        this.quickPick = quickPick;
    }

}
