package Project.recivers;

/**
 * @author github.kloping
 */
public class RewardReceiver {
//    private Map<Long, List<InviteRecord>> recordMap = new LinkedHashMap<>();
//    public static final String PATH = "./conf/invite-recorde.hml";

    public RewardReceiver() {
//        System.out.println("==============" + this.getClass().getName() + "===============");
//        try {
//            recordMap = HMLObject.parseObject(FileUtils.getStringFromFile(PATH), recordMap.getClass());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        MemberJoinedBroadcast.INSTANCE.add(new MemberJoinedBroadcast.MemberJoinedReceiver() {
//            @Override
//            public void onReceive(long q, long g, long iq) {
//                if (iq < 0) return;
//                if (recordMap.containsKey(iq)) {
//                    for (InviteRecord record : recordMap.get(iq)) {
//                        if (record.q == q) {
//                            return;
//                        }
//                    }
//                }
//                FrameUtils.SERVICE.schedule(() -> {
//                    boolean k = BootstarpResource.getBot().getGroup(g).contains(q);
//
//                    if (k) {
//                        StringBuilder sb = new StringBuilder();
//                        sb.append(Tool.INSTANCE.at(iq)).append(".\n").append("邀请奖励:").append("2w积分");
//                        MessageUtils.INSTANCE.sendMessageInGroup(sb.toString(), g);
//                        DataBase.addScore(20000, iq);
//                        InviteRecord record = new InviteRecord();
//                        record.setGid(g);
//                        record.setQ(q);
//                        record.setTime(System.currentTimeMillis());
//                        record.setIq(iq);
//                        MapUtils.append(recordMap, iq, record, LinkedList.class);
//                        FileInitializeValue.putValues(PATH, recordMap);
//                    }
//
//                }, 5, TimeUnit.MINUTES);
//            }
//        });
    }

//    public static class InviteRecord {
//        public Long q;
//        public Long iq;
//        public Long time;
//        public Long gid;
//
//        public Long getTime() {
//            return time;
//        }
//
//        public void setTime(Long time) {
//            this.time = time;
//        }
//
//        public Long getGid() {
//            return gid;
//        }
//
//        public void setGid(Long gid) {
//            this.gid = gid;
//        }
//
//        public Long getQ() {
//            return q;
//        }
//
//        public void setQ(Long q) {
//            this.q = q;
//        }
//
//        public Long getIq() {
//            return iq;
//        }
//
//        public void setIq(Long iq) {
//            this.iq = iq;
//        }
//    }
}
