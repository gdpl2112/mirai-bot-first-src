package io.github.kloping.mirai0.Main.iutils;

import Project.commons.SpGroup;
import Project.commons.SpUser;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;

import java.util.HashMap;
import java.util.Map;

import static io.github.kloping.mirai0.Main.BootstarpResource.BOT;

/**
 * @author github-kloping
 */
public class MemberUtils {
    public static final Map<Long, Long> RECENT_SPEECHES = new HashMap<>();

    public static SpUser getUser(long q) {
        if (SpUser.get(q) == null) {
            for (Group group : BOT.getGroups()) {
                if (group.contains(q)) {
                    Member member = group.get(q);
                    SpUser user = SpUser.create(q,
                            group.getId(),
                            member.getNameCard(),
                            member.getNick()
                    );
                    return user;
                } else {
                    SpUser user = SpUser.create(q,
                            group.getId(), "default", "default"
                    );
                    return user;
                }
            }
        }
        return SpUser.get(q);
    }

    public static String getNameFromGroup(long id, SpGroup group) {
        String name = "";
        try {
            Group group1 = BOT.getGroup(group.getId());
            Member member = group1.get(id);
            name = member.getNameCard();
            if (name == null || name.isEmpty())
                name = member.getNick();
            return name;
        } catch (Exception e) {
            return id + "";
        }
    }

    public static String getName(long id) {
        String name = "";
        try {
            for (Group group : BOT.getGroups()) {
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

    public static long getRecentSpeechesGid(long qid) {
        return RECENT_SPEECHES.get(qid);
    }
}
