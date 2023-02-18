package Project.aSpring.mcs.controllers;

import Project.controllers.auto.ControllerSource;
import Project.dataBases.GameDataBase;
import io.github.kloping.mirai0.commons.PersonInfo;
import io.github.kloping.mirai0.commons.SpUser;
import io.github.kloping.mirai0.unitls.drawers.Drawer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static Project.dataBases.GameDataBase.getInfo;

/**
 * @author github.kloping
 */
@RestController
public class DetailController {

    public static final Map<Integer, Long> RID2QID = new HashMap<>();

    @GetMapping("/infoImg")
    public String infoImg(@RequestParam("qid") Integer qid) {
        Long q = RID2QID.get(qid);
        PersonInfo is = getInfo(q);
        File file = new File(Drawer.drawInfo(is));
        return file.getName();
    }

    @GetMapping("/info")
    public PersonInfo info(@RequestParam("qid") Integer qid) {
        PersonInfo is = getInfo(RID2QID.get(qid));
        return is;
    }

    @GetMapping("/getNameById")
    public String getNameById(@RequestParam("id") Integer id) {
        return GameDataBase.getNameById(id);
    }

    @GetMapping("/getBag")
    public Object getBagById(@RequestParam("qid") Integer qid) {
        return ControllerSource.gameController2.bgs0(SpUser.get(RID2QID.get(qid)), "0");
    }
}
