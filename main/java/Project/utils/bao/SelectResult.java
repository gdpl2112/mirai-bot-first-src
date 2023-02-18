package Project.utils.bao;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author github.kloping
 */
@Data
@Accessors(chain = true)
public class SelectResult {
    public Number[] ats;
    public final String oStr;
    public String str;

    public SelectResult(String oStr) {
        this.oStr = oStr;
    }
}
