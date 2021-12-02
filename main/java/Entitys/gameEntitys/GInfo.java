package Entitys.gameEntitys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import static Project.ASpring.SpringBootResource.gInfoMapper;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class GInfo {
    private long qid = -1;
    private int masterPoint = 0;
    /**
     * 修炼次数
     */
    private int xlc = 0;
    /**
     * 进入次数
     */
    private int joinc = 0;
    /**
     * 获得物品次数
     */
    private int gotc = 0;
    /**
     * 失去物品次数
     */
    private int lostc = 0;

    public GInfo addQid() {
        this.qid++;
        return this;
    }

    public GInfo addQid(int i) {
        this.qid += i;
        return this;
    }

    public GInfo addMasterPoint() {
        this.masterPoint++;
        return this;
    }

    public GInfo addMasterPoint(int i) {
        this.masterPoint += i;
        return this;
    }

    public GInfo addXlc() {
        this.xlc++;
        return this;
    }

    public GInfo addXlc(int i) {
        this.xlc += i;
        return this;
    }

    public GInfo addJoinc() {
        this.joinc++;
        return this;
    }

    public GInfo addJoinc(int i) {
        this.joinc += i;
        return this;
    }

    public GInfo addGotc() {
        this.gotc++;
        return this;
    }

    public GInfo addGotc(int i) {
        this.gotc += i;
        return this;
    }

    public GInfo addLostc() {
        this.lostc++;
        return this;
    }

    public GInfo addLostc(int i) {
        this.lostc += i;
        return this;
    }

    public void apply() {
        UpdateWrapper<GInfo> wrapper = new UpdateWrapper<>();
        wrapper.eq("qid", this.getQid());
        if (gInfoMapper.update(this, wrapper) == 0) {
            gInfoMapper.insert(this);
        }
    }

    public static GInfo getInstance(long qid) {
        GInfo gInfo = gInfoMapper.selectOne(new QueryWrapper<GInfo>().eq("qid", qid));
        if (gInfo != null) return gInfo;
        return new GInfo().setQid(qid);
    }

//
//    public static void main(String[] args) {
//        summon(GInfo.class);
//    }
//
//    public static void summon(Class<?> cla) {
//        Field[] fields = cla.getDeclaredFields();
//        for (Field field : fields) {
//            Class c1 = ObjectUtils.baseToPack(field.getType());
//            if (c1 == Long.class || c1 == Integer.class) {
//                System.out.print("\tpublic ");
//                System.out.print(cla.getSimpleName());
//                System.out.print(" add");
//                System.out.print(field.getName().substring(0, 1).toUpperCase());
//                System.out.print(field.getName().substring(1));
//                System.out.println("() {");
//                System.out.print("\t\tthis.");
//                System.out.print(field.getName());
//                System.out.println("++;");
//                System.out.println("\t\treturn this;");
//                System.out.print("\t}");
//                System.out.println();
//                System.out.println();
//                System.out.print("\tpublic ");
//                System.out.print(cla.getSimpleName());
//                System.out.print(" add");
//                System.out.print(field.getName().substring(0, 1).toUpperCase());
//                System.out.print(field.getName().substring(1));
//                System.out.println("(int i) {");
//                System.out.print("\t\tthis.");
//                System.out.print(field.getName());
//                System.out.println(" += i;");
//                System.out.println("\t\treturn this;");
//                System.out.print("\t}");
//                System.out.println();
//                System.out.println();
//            }
//        }
//    }
}
