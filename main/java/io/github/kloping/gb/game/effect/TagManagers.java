package io.github.kloping.gb.game.effect;

import io.github.kloping.gb.game.e0.TagPack;
import io.github.kloping.map.MapUtils;

import java.util.*;

public class TagManagers {
    public static Map<String, TagManager> managerMap = new HashMap<>();

    public static TagManager getTagManager(String qid) {
        if (managerMap.containsKey(qid)) {
            return managerMap.getOrDefault(qid, new TagManager(qid));
        } else {
            managerMap.put(qid, new TagManager(qid));
            return getTagManager(qid);
        }
    }

    public static class TagManager {
        public static final HashSet<TagPack> EMPTY_HASH_SET = new HashSet();
        private String qid;
        private Map<String, Set<TagPack>> map = new HashMap<>();

        public TagManager(String qid) {
            this.qid = qid;
        }

        public synchronized Long getValue(String tag) {
            Long v = 0L;
            Iterator<TagPack> tagPackIterator = map.getOrDefault(tag, EMPTY_HASH_SET).iterator();
            while (tagPackIterator.hasNext()) {
                TagPack tagPack = tagPackIterator.next();
                if (!tagPack.over()) {
                    v += tagPack.getValue();
                } else {
                    tagPackIterator.remove();
                }
            }
            return v;
        }

        public synchronized TagManager eddValue(String tag, Long v) {
            long v0 = v;
            Iterator<TagPack> tagPackIterator = map.getOrDefault(tag, EMPTY_HASH_SET).iterator();
            while (tagPackIterator.hasNext()) {
                TagPack tagPack = tagPackIterator.next();
                if (!tagPack.over()) {
                    long v1 = tagPack.getValue();
                    if (v1 > v0) {
                        tagPack.eddValue(v0);
                    } else {
                        v0 -= v1;
                        tagPackIterator.remove();
                    }
                } else {
                    tagPackIterator.remove();
                }
            }
            return this;
        }

        public synchronized TagManager eddValue(String tag) {
            Iterator<TagPack> tagPackIterator = map.getOrDefault(tag, EMPTY_HASH_SET).iterator();
            while (tagPackIterator.hasNext()) {
                TagPack tagPack = tagPackIterator.next();
                tagPackIterator.remove();
            }
            return this;
        }

        public synchronized Boolean contains(String tag) {
            return getValue(tag) > 0;
        }

        public synchronized void addTag(TagPack tagPack) {
            MapUtils.appendSet(map, tagPack.getTAG(), tagPack);
        }

        public void removeAll() {
            map.clear();
        }
    }
}
