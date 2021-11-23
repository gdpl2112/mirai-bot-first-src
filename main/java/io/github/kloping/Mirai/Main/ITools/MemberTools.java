package io.github.kloping.Mirai.Main.ITools;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;

import static io.github.kloping.Mirai.Main.Resource.bot;

public class MemberTools {
    public static synchronized String getNameFromGroup(long id, Entitys.Group group) {
        String name = "";
        try {
            Group group1 = bot.getGroup(group.getId());
            Member member = group1.get(id);
            name = member.getNameCard();
            if (name == null || name.isEmpty())
                name = member.getNick();
            return name;
        } catch (Exception e) {
            return id + "";
        }
    }

    public static synchronized String getName(long id) {
        String name = "";
        try {
            for (Group group : bot.getGroups()) {
                if (group.contains(id)) {
                    Member member = group.get(id);
                    name = member.getNick();
                    return name;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id + "";
    }
}
