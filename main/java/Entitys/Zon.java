package Entitys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Zon {
    private Integer id;
    private Number qq;
    private Integer level = 0;
    private Integer times = 0;
    private Integer xper = 0;
}
