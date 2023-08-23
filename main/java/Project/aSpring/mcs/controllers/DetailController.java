package Project.aSpring.mcs.controllers;

import Project.commons.SpUser;
import Project.controllers.auto.ControllerSource;
import Project.dataBases.GameDataBase;
import Project.aSpring.dao.PersonInfo;
import Project.utils.drawers.Drawer;
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
