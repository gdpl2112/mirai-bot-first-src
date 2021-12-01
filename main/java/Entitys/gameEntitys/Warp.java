package Entitys.gameEntitys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Warp {
    private Number id = -1;
    private Number bindQ = -1;
    private Number master = -1;
    private Number prentice = -1;
    public Warp setId(String id) {
        try {
            this.id = Long.parseLong(id.trim());
        } catch (Exception e) {
        }
        return this;
    }
}
