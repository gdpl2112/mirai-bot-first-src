package Project.utils;

import Project.utils.bao.SelectResult;

import java.util.HashSet;
import java.util.Set;

import static Project.commons.rt.ResourceSet.FinalNormalString.EMPTY_STR;
import static Project.controllers.auto.ControllerSource.challengeDetailService;
import static io.github.kloping.mirai0.Main.BootstarpResource.BOT;

/**
 * @author github.kloping
 */
public class GameUtils {
    public static SelectResult getAllSelect(long qid, String str) {
        SelectResult result = new SelectResult(new String(str));
        Set<Number> numbers = new HashSet<>();
        while (true) {
            if (str.contains("#")) {
                str = str.replaceAll("#", EMPTY_STR);
                numbers.add(-2);
            }
            Long l1 = Project.utils.Utils.getAtFromString(str);
            str = str.replaceFirst("\\[@" + (l1 == BOT.getId() ? "me" : l1) + "]", EMPTY_STR);
            if (l1 <= 0) {
                break;
            } else {
                //过滤挑战
                if (challengeDetailService.isTemping(l1)) {
                    if (challengeDetailService.isTemping(qid)) {
                        if (challengeDetailService.challenges.Q2Q.get(qid) == l1.longValue()) {
                            numbers.add(l1);
                        }
                    }
                } else if (challengeDetailService.isTemping(qid)) {
                    if (challengeDetailService.isTemping(l1)) {
                        if (challengeDetailService.challenges.Q2Q.get(qid) == l1.longValue()) {
                            numbers.add(l1);
                        }
                    }
                } else {
                    numbers.add(l1);
                }
            }
        }
        Number[] ats = numbers.toArray(new Number[0]);
        result.setStr(str);
        result.setAts(ats);
        return result;
    }
}
