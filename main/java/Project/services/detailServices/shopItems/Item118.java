package Project.services.detailServices.shopItems;

import io.github.kloping.mirai0.Main.ITools.MessageTools;
import io.github.kloping.mirai0.commons.PersonInfo;

import static Project.dataBases.GameDataBase.getInfo;
import static io.github.kloping.mirai0.Main.ITools.MemberTools.getRecentSpeechesGid;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalFormat.AT_FORMAT;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalFormat.AT_FORMAT0;
import static io.github.kloping.mirai0.commons.resouce_and_tool.ResourceSet.FinalString.NEWLINE;

/**
 * @author github.kloping
 */
public class Item118 implements Runnable {
    public long qid;

    public Item118(long who) {
        this.qid = who;
    }

    @Override
    public void run() {
        long gid = getRecentSpeechesGid(qid);
        PersonInfo pInfo;
        pInfo = getInfo(qid);
        long hp = pInfo.getHp();
        long hl = pInfo.getHl();
        long hj = pInfo.getHj();
        MessageTools.sendMessageInGroup(String.format(AT_FORMAT0, qid) + NEWLINE + "开始记录", gid);
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pInfo = getInfo(qid);
        if (pInfo.getHp() > 0) {
            pInfo.setHp(hp).setHl(hl).setHj(hj).apply();
            MessageTools.sendMessageInGroup(String.format(AT_FORMAT0, qid) + NEWLINE + "刚刚好像发生了什么", gid);
        } else {
            MessageTools.sendMessageInGroup(String.format(AT_FORMAT0, qid) + NEWLINE + "药效丢失", gid);
        }
    }
}
