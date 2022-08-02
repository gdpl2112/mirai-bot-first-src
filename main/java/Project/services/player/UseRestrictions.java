package Project.services.player;

import Project.controllers.auto.TimerController;
import Project.services.detailServices.GameJoinDetailService;
import io.github.kloping.mirai0.commons.GhostObj;

import java.util.HashMap;
import java.util.Map;

/**
 * 使用限制
 *
 * @author github.kloping
 */
public class UseRestrictions {
    public static final Map<Integer, Integer> USE_INTEGER_MAP = new HashMap<>();
    public static final Map<Long, Integer> USE_RECORD = new HashMap<>();
    public static final Integer MAX_MEET_C = 7;

    static {
        TimerController.ZERO_RUNS.add(() -> {
            USE_RECORD.clear();
        });
        USE_INTEGER_MAP.put(117, 2);
//        USE_INTEGER_MAP.put(116, 5);
    }

    public static Integer getUseRestrictions(int id) {
        return USE_INTEGER_MAP.get(id);
    }

    public static boolean cant(long qid, int id) {
        GhostObj ghostObj = GameJoinDetailService.getGhostObjFrom(qid);
        if (ghostObj != null) {
            if (ghostObj.getTips() >= MAX_MEET_C)
                return true;
        }
        return USE_RECORD.containsKey(qid) && USE_INTEGER_MAP.containsKey(id)
                ? USE_RECORD.get(qid) >= USE_INTEGER_MAP.get(id) : false;
    }

    public static void record(long qid, int id) {
        int n0 = 0;
        if (USE_RECORD.containsKey(qid)) {
            n0 = USE_RECORD.get(qid);
        }
        n0++;
        USE_RECORD.put(qid, n0);
        GhostObj ghostObj = GameJoinDetailService.getGhostObjFrom(qid);
        if (ghostObj != null) {
            ghostObj.setNc(ghostObj.getTips() + 1);
        }
    }
}
