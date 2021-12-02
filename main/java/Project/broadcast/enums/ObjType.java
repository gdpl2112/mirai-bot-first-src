package Project.broadcast.enums;

public enum ObjType {
    got(2),
    buy(3), use(4),
    transGot(5),
    transLost(6),
    sell(7),
    un(8)
    ;

    public int v = -1;

    ObjType(int v) {
        this.v = v;
    }

    public static ObjType valueOf(int m) {
        for (ObjType value : ObjType.values())
            if (value.v == m) return value;
        return null;
    }
}