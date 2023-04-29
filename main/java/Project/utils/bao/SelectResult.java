package Project.utils.bao;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author github.kloping
 */
@Data
@Accessors(chain = true)
public class SelectResult {
    public final String oStr;
    public Number[] ats;
    public String str;

    public SelectResult(String oStr) {
        this.oStr = oStr;
    }
}
