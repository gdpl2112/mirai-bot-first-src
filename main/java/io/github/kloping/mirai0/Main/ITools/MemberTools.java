package io.github.kloping.mirai0.Main.ITools;

import io.github.kloping.mirai0.Entitys.User;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;

import static io.github.kloping.mirai0.Main.Resource.bot;

/**
 * @author github-kloping
 */
public class MemberTools {
    public static User getUser(long q) {
        if (User.get(q) == null) {
            for (Group group : bot.getGroups()) {
                if (group.contains(q)) {
                    Member member = group.get(q);
                    User user = User.create(q,
                            group.getId(),
                            member.getNameCard(),
                            member.getNick()
                    );
                    return user;
                }
            }
        }
        return User.get(q);
    }

    public static synchronized String getNameFromGroup(long id, io.github.kloping.mirai0.Entitys.Group group) {
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
