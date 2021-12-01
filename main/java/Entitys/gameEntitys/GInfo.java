package Entitys.gameEntitys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class GInfo {
    private long qId = -1;
    private int masterPoint;
    private int v1;

    public GInfo addMasterPoint(int n) {
        masterPoint = masterPoint + n;
        return this;
    }

    public GInfo addMasterPoint() {
        masterPoint = masterPoint + 1;
        return this;
    }
}
