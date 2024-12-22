package io.github.kloping.kzero.spring.service;

import io.github.kloping.date.DateUtils;
import io.github.kloping.kzero.main.KlopZeroMainThreads;
import io.github.kloping.kzero.main.api.KZeroBot;
import io.github.kloping.kzero.spring.dao.Vocabulary;
import io.github.kloping.kzero.spring.mapper.VocabularyMapper;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.message.data.ForwardMessageBuilder;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author github kloping
 * @date 2024/12/3-13:50
 */
@Service
@ConditionalOnProperty(prefix = "study", name = "enable", havingValue = "true")
public class StudyService {

    private static final SimpleDateFormat SF_0 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Autowired
    RestTemplate template;

    @Value("${study.enable:false}")
    Boolean enable;

    @Autowired
    VocabularyMapper vocabularyMapper;

    @Scheduled(cron = "${study.cron:0 48 8 * * ?}")
    public void sendWords() {
        if (!enable) return;
        Set<String> vws = new HashSet<>();
        Bot nb = null;
        for (KZeroBot bot : KlopZeroMainThreads.BOT_MAP.values()) {
            if (bot != null && bot.getSelf() instanceof Bot) {
                nb = (Bot) bot.getSelf();
            }
        }
        if (nb == null) return;
        ForwardMessageBuilder builder = new ForwardMessageBuilder(nb.getAsFriend());

        builder.add(nb.getAsFriend(), new PlainText(String.format("每日单词[%s]", SF_0.format(new Date()))));

        while (vws.size() < 20) {
            String vv = template.getForObject("http://47.120.68.44:999/cihui/", String.class);
            vv = vv.trim();
            if (vws.contains(vv)) continue;
            Integer ei = vv.indexOf(" ");
            String e = vv.substring(0, ei).trim();
            String c = vv.substring(ei).trim();
            Vocabulary vocabulary = vocabularyMapper.selectById(e);
            if (vocabulary != null) continue;
            vocabulary = new Vocabulary();
            vocabulary.setWord(e);
            vocabulary.setMean(c);
            vocabulary.setTimestamp(String.format("%s-%s-%s", DateUtils.getYear(), DateUtils.getMonth(), DateUtils.getDay()));
            vocabularyMapper.insert(vocabulary);
            vws.add(vv);
            builder.add(nb.getAsFriend(), new PlainText(vv));
        }
        Message msg = builder.build();
        nb.getGroup(633712602L).sendMessage(msg);
    }
}
