package Project.aSpring;

import Project.aSpring.dao.WhInfo;
import Project.utils.Utils;
import io.github.kloping.MySpringTool.h1.impl.component.PackageScannerImpl;
import io.github.kloping.MySpringTool.interfaces.component.PackageScanner;
import io.github.kloping.common.Public;
import io.github.kloping.mirai0.Main.BootstarpResource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static Project.aSpring.SpringBootResource.environment;
import static Project.aSpring.SpringBootResource.init;

/**
 * @author github-kloping
 */
@SpringBootApplication(scanBasePackages = {"Project.aSpring.mcs"})
@MapperScan("Project.aSpring.mcs.mapper")
public class SpringStarter {

    public static final List<Runnable> STARTED_RUNNABLE = new ArrayList<>();

    public static ConfigurableApplicationContext configuration;

    public static void main(String[] args) {
        configuration = new SpringApplication(new Class[]{SpringStarter.class}).run(args);
        environment = configuration.getEnvironment();
        init();
        over();
        for (Runnable runnable : STARTED_RUNNABLE) {
            Public.EXECUTOR_SERVICE.submit(runnable);
        }
    }

    private static void over() {
        DataSource dataSource = SpringStarter.configuration.getBean(DataSource.class);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        try {
            try0(jdbcTemplate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try1(jdbcTemplate);
    }

    private static void try0(JdbcTemplate jdbcTemplate) throws Exception {
        PackageScanner scanner = new PackageScannerImpl(true);
        for (Class<?> dclass : scanner.scan(SpringStarter.class, BootstarpResource.class.getClassLoader(),
                "Project.aSpring.dao")) {
            try {
                String sql = Utils.CreateTable.createTable(dclass);
                int state = jdbcTemplate.update(sql);
                if (state > 0) System.out.println(sql);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //========================
        boolean k0 = false;
        for (Map<String, Object> e0 : jdbcTemplate.queryForList("DESC `zong`")) {
            String name = e0.get("Field").toString();
            if ("active".equals(name)) {
                k0 = true;
            }
        }
        if (!k0) {
            System.out.println("zong添加字段");
            jdbcTemplate.update("ALTER TABLE `zong` ADD COLUMN `active` INT(9) NOT NULL COMMENT '活跃点数' AFTER `pub`;");
        }
        //========
        k0 = false;
        for (Map<String, Object> e0 : jdbcTemplate.queryForList("DESC `zon`")) {
            String name = e0.get("Field").toString();
            if ("active".equals(name)) {
                k0 = true;
            }
        }
        if (!k0) {
            System.out.println("zon添加字段");
            jdbcTemplate.update("ALTER TABLE `zon` ADD COLUMN `active` INT NOT NULL COMMENT '活跃点数' AFTER `xper`;");
        }
        //========
        k0 = false;
        for (Map<String, Object> e0 : jdbcTemplate.queryForList("DESC `auto_reply`")) {
            String name = e0.get("Field").toString();
            if ("gid".equals(name)) {
                k0 = true;
            }
        }
        if (!k0) {
            System.out.println("auto_reply添加字段");
            jdbcTemplate.update("ALTER TABLE `auto_reply` ADD COLUMN `gid` BIGINT NOT NULL COMMENT '分群' AFTER `id`;");
        }
        //========
        k0 = false;
        for (Map<String, Object> e0 : jdbcTemplate.queryForList("DESC `person_info`")) {
            String name = e0.get("Field").toString();
            if ("p".equals(name)) {
                k0 = true;
            }
        }
        if (!k0) {
            System.out.println("person_info添加字段");
            jdbcTemplate.update("ALTER TABLE `person_info`\n" +
                    "\tADD COLUMN `p` INT NOT NULL COMMENT '武魂信息指向' DEFAULT '1' AFTER `bgk`;");
        }
        //========
        k0 = false;
        for (Map<String, Object> e0 : jdbcTemplate.queryForList("DESC `skill_info`")) {
            String name = e0.get("Field").toString();
            if ("p".equals(name)) {
                k0 = true;
            }
        }
        if (!k0) {
            System.out.println("skill_info添加字段");
            jdbcTemplate.update("ALTER TABLE `skill_info`\n" +
                    "\tADD COLUMN `p` INT NOT NULL DEFAULT '1' AFTER `state`;");
        }
        //========
        k0 = false;
        for (Map<String, Object> e0 : jdbcTemplate.queryForList("DESC `hhpz`")) {
            String name = e0.get("Field").toString();
            if ("p".equals(name)) {
                k0 = true;
            }
        }
        if (!k0) {
            System.out.println("hhpz添加字段");
            jdbcTemplate.update("ALTER TABLE `hhpz`\n" +
                    "\tADD COLUMN `p` INT NOT NULL DEFAULT '1' AFTER `state`;");
        }
        //========
        k0 = false;
        for (Map<String, Object> e0 : jdbcTemplate.queryForList("DESC `upup`")) {
            String name = e0.get("Field").toString();
            if ("p".equals(name)) {
                k0 = true;
            }
        }
        if (!k0) {
            System.out.println("hhpz添加字段");
            jdbcTemplate.update("ALTER TABLE `upup`\n" +
                    "\tADD COLUMN `p` INT NOT NULL DEFAULT '1' AFTER `state`;");
        }
        jdbcTemplate.execute("\n" +
                "-- 导出  表 raffle 结构\n" +
                "CREATE TABLE IF NOT EXISTS `raffle` (\n" +
                "  `qid` bigint(20) NOT NULL,\n" +
                "  `c1` int(11) NOT NULL DEFAULT '0',\n" +
                "  `c2` int(11) NOT NULL DEFAULT '0',\n" +
                "  UNIQUE KEY `qid` (`qid`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='抽奖记录';\n");
    }

    private static void try1(JdbcTemplate jdbcTemplate) {
        boolean k0 = false;
        for (Map<String, Object> e0 : jdbcTemplate.queryForList("DESC `person_info`")) {
            String name = e0.get("Field").toString();
            if ("att".equals(name)) {
                k0 = true;
            }
        }
        if (k0) {
            jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS wh_info ( \n" +
                    "`att`BIGINT(18) NOT NULL,\n" +
                    " `hl`BIGINT(18) NOT NULL,\n" +
                    " `hll`BIGINT(18) NOT NULL,\n" +
                    " `hp`BIGINT(18) NOT NULL,\n" +
                    " `hpl`BIGINT(18) NOT NULL,\n" +
                    " `hj`BIGINT(18) NOT NULL,\n" +
                    " `hj_l`BIGINT(18) NOT NULL,\n" +
                    " `level`INT(9) NOT NULL,\n" +
                    " `wh`INT(9) NOT NULL,\n" +
                    " `wh_type`INT(9) NOT NULL,\n" +
                    " `xp`BIGINT(18) NOT NULL,\n" +
                    " `xp_l`BIGINT(18) NOT NULL,\n" +
                    " `qid`BIGINT(18) NOT NULL,\n" +
                    " `p`INT(9) NOT NULL\n" +
                    " );");
            for (Map<String, Object> m : jdbcTemplate.queryForList("SELECT * FROM `person_info`")) {
                WhInfo whInfo = new WhInfo();
                whInfo.setQid(Long.valueOf(m.get("name").toString()));
                whInfo.setP(1);
                whInfo.setLevel((Integer) m.get("level"));
                whInfo.setWh((Integer) m.get("wh"));
                whInfo.setWhType((Integer) m.get("wh_type"));
                whInfo.setAtt((Long) m.get("att"));
                whInfo.setXp((Long) m.get("xp"));
                whInfo.setXpL((Long) m.get("xp_l"));
                whInfo.setHp((Long) m.get("hp"));
                whInfo.setHpl((Long) m.get("hpl"));
                whInfo.setHl((Long) m.get("hl"));
                whInfo.setHll((Long) m.get("hll"));
                whInfo.setHj((Long) m.get("hj"));
                whInfo.setHjL((Long) m.get("hj_l"));
                SpringBootResource.getWhInfoMapper().insert(whInfo);
            }
            jdbcTemplate.update("ALTER TABLE `person_info`\n" +
                    "\tDROP COLUMN `level`,\n" +
                    "\tDROP COLUMN `att`,\n" +
                    "\tDROP COLUMN `hp`,\n" +
                    "\tDROP COLUMN `hpl`,\n" +
                    "\tDROP COLUMN `hl`,\n" +
                    "\tDROP COLUMN `hll`,\n" +
                    "\tDROP COLUMN `hj`,\n" +
                    "\tDROP COLUMN `hj_l`,\n" +
                    "\tDROP COLUMN `xp`,\n" +
                    "\tDROP COLUMN `xp_l`,\n" +
                    "\tDROP COLUMN `wh`,\n" +
                    "\tDROP COLUMN `wh_type`;");
            System.out.println("数据库结构更新完成");
        }
    }
}
