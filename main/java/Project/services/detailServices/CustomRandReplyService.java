package Project.services.detailServices;

import Project.broadcast.normal.MessageBroadcast;
import io.github.kloping.MySpringTool.annotations.Entity;
import io.github.kloping.file.FileUtils;
import io.github.kloping.mirai0.Main.iutils.MessageUtils;
import io.github.kloping.mirai0.Main.BootstarpResource;
import Project.commons.eEntitys.CustomElement;
import Project.commons.eEntitys.CustomReplyGroup;
import io.github.kloping.serialize.HMLObject;

import java.io.File;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author github-kloping
 * @version 1.0
 */
@Entity
public class CustomRandReplyService {
    public static List<CustomReplyGroup> customReplyGroups = new CopyOnWriteArrayList<>();
    private static String path;
    private static Integer ID = 0;

    public CustomRandReplyService() {
        path = new File(BootstarpResource.datePath, "customRandReply").getAbsolutePath();
        init();
    }

    private void init() {
        if (new File(path).exists()) {
            for (File file : new File(path).listFiles()) {
                CustomReplyGroup customReplyGroup = null;
                try {
                    customReplyGroup = HMLObject.parseObject(FileUtils.getStringFromFile(file.getAbsolutePath()), CustomReplyGroup.class);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                customReplyGroups.add(customReplyGroup);
                if (customReplyGroup.getId() > ID) {
                    ID = customReplyGroup.getId() + 1;
                }
            }
            init1();
        }
    }

    private void init1() {
        MessageBroadcast.INSTANCE.add(new MessageBroadcast.MessageReceiver() {
            @Override
            public void onReceive(long qid, long gid, String context) {
                for (CustomReplyGroup customReplyGroup : customReplyGroups) {
                    for (CustomElement key : customReplyGroup.getKeys()) {
                        if (key.getContext().equals(context)) {
                            CustomElement customElement = customReplyGroup.get();
                            MessageUtils.INSTANCE.sendMessageInGroupWithAt(customElement.getContext(), gid, qid);
                            break;
                        }
                    }
                }
            }
        });
    }

    public boolean save(CustomReplyGroup group) {
        if (group == null) {
            return false;
        }
        try {
            group.setId(ID++);
            File file = new File(path, group.getId().toString());
            String s = HMLObject.toHMLString(group);
            FileUtils.putStringInFile(s, file);
            customReplyGroups.add(group);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
