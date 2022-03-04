package io.github.kloping.mirai0.Main;

import Project.dataBases.ZongMenDataBase;
import io.github.kloping.mirai0.Entitys.gameEntitys.Zon;
import io.github.kloping.mirai0.Entitys.gameEntitys.Zong;

import java.io.File;

import static Project.aSpring.SpringBootResource.getZonMapper;
import static Project.aSpring.SpringBootResource.getZongMapper;
import static Project.dataBases.ZongMenDataBase.*;

/**
 * @author github.kloping
 */
public class Temp {
    public static void main(String[] args) {
        Resource.Switch.AllK=false;
        File[] files = new File(ZongMenDataBase.path).listFiles();
        for (File file1 : files) {
            if (file1.isDirectory()) {
                Zong zong = getZongInfoFromFile(Integer.valueOf(file1.getName()));
                for (Number number : zong.getMember()) {
                    Zon zon = getZonInfo(number.longValue());
                    if (zon == null || zon.getQq() == null) {
                        System.err.println(zong.getId());
                    } else {
                        try {
                            getZonMapper().insert(zon);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                getZongMapper().insert(zong);
            } else {
                continue;
            }
        }
        System.exit(0);
    }
}
