package temp;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.GroupSettings;
import net.mamoe.mirai.data.GroupInfo;

import java.lang.reflect.Field;

public class M2 {
    public static void main(String[] args) {

    }

    public static void m1(Group group) {
        try {
            Class cla;
            Field[] fields;
            Field field;
            cla = group.getClass();
            fields = cla.getDeclaredFields();
            field = cla.getDeclaredField("uin");
            field.setAccessible(true);
            System.out.println(field.get(group));
//
//           GroupSettings gs = group.getSettings();
//           cla = gs.getClass();
//           fields = cla.getDeclaredFields();
//           field = cla.getDeclaredField("groupInfo");
//           field.setAccessible(true);
//           GroupInfo groupInfo = (GroupInfo) field.get(gs);
//           cla = group.getClass();
//           field = cla.getDeclaredField("uin");
//           field.setAccessible(true);
//           Object uin = field.get(groupInfo);
//           System.out.println(uin);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
