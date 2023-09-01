package io.github.kloping.kzero.main;

import io.github.kloping.MySpringTool.annotations.CommentScan;
import io.github.kloping.kzero.mirai.MiraiStater;

/**
 * @author github.kloping
 */
@CommentScan(path = "io.github.kzero")
public class Main {
    public static void main(String[] args) {
        KZeroMainThreads threads = new KZeroMainThreads();
        threads.add(new MiraiStater());
        threads.run();
    }
}
