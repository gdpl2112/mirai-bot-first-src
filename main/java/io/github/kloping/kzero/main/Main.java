package io.github.kloping.kzero.main;

import io.github.kloping.MySpringTool.annotations.CommentScan;
import io.github.kloping.kzero.guilds.GuildStater;
import io.github.kloping.kzero.mirai.MiraiStater;

/**
 * @author github.kloping
 */
@CommentScan(path = "io.github.kloping.kzero")
public class Main {
    public static void main(String[] args) {
        KZeroMainThreads threads = new KZeroMainThreads();
        threads.add(new MiraiStater());
        threads.add(new GuildStater());
        threads.run();
    }
}
