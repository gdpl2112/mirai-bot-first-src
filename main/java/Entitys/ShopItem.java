package Entitys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ShopItem {
    private Integer id;
    private Number who;
    private Integer itemId;
    private Integer num;
    private Long time;
    private Number price=1;
}
