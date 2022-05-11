package Project.interfaces.Iservice;


import io.github.kloping.mirai0.commons.Group;

/**
 * @author github-kloping
 */
public interface IGameService {
    /**
     * 修炼
     *
     * @param who
     * @return
     */
    String xl(Long who);

    /**
     * 信息
     *
     * @param who
     * @return
     */
    String info(Long who);

    /**
     * 升级
     *
     * @param who
     * @return
     */
    String upUp(Long who);

    /**
     * 升级属性
     *
     * @param who
     * @return
     */
    String upTrue(Long who);

    /**
     * 觉醒武魂
     *
     * @param who
     * @return
     */
    String openEyeWh(Long who);

    /**
     * 获取背包
     *
     * @param who
     * @return
     */
    String[] getBags(Long who);

    /**
     * 获取背包
     *
     * @param who
     * @return
     */
    String[] getBags(String who);

    /**
     * buy 金魂币
     *
     * @param who
     * @param num
     * @return
     */
    String buyGold(Long who, long num);

    /**
     * 显示魂环
     *
     * @param who
     * @return
     */
    String showHh(Long who);

    /**
     * 吸收魂环
     *
     * @param who
     * @param id
     * @return
     */
    String parseHh(Long who, int id);

    /**
     * 攻击某人
     *
     * @param who
     * @param whos
     * @param group
     * @return
     */
    String att(Long who, Long whos, Group group);

    /**
     * 换积分
     *
     * @param who
     * @param num
     * @return
     */
    String getScoreFromGold(Long who, long num);

    /**
     * 取名封号
     *
     * @param who
     * @param name
     * @param group
     * @return
     */
    String makeSname(Long who, String name, Group group);

    /**
     * 转生
     *
     * @param id
     * @return
     */
    String returnA(long id);

    /**
     * 双修
     *
     * @param qq
     * @return
     */
    String xl2(long qq);

    /**
     * 融合
     *
     * @param q1
     * @param q2
     * @param group
     * @return
     */
    String fusion(Long q1, Long q2, Group group);

    /**
     * 详细信息
     *
     * @param q
     * @return
     */
    String detailInfo(long q);

    /**
     * 收徒
     *
     * @param q
     * @param q2
     * @return
     */
    String shouTu(long q, long q2);

    /**
     * 出师
     *
     * @param q
     * @return
     */
    String chuShi(long q);

    /**
     * 升级魂环
     *
     * @param q
     * @param st
     * @return
     */
    String upHh(long q, int st);

    /**
     * 精神攻击
     *
     * @param q
     * @param q2
     * @param br
     * @return
     */
    String attByHj(long q, long q2, int br);

    /**
     * 出徒
     *
     * @param q
     * @return
     */
    String chuTu(long q);
}
