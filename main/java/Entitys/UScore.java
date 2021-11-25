package Entitys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UScore implements Serializable {
    private Long score = Long.valueOf(1000);
    private Long times = 0l;
    private Long fz = 0l;
    private Long sTimes = 0l;
    private Long days = 0L;
    private Long sScore = 200L;
    private Long K = -1L;
    private Long day = -1L;
    private Long timesDay = -1L;
    private Long who;
}