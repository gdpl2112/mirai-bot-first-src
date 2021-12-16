package temp;

import static Project.Tools.Tool.randA;

public class M2 {
    public static void main(String[] args) {
//        System.out.println("测试所用的临时代码");
        int idMin = 501,idMax=521;
        for (int i = 0; i < 1000; i++) {
            System.out.println(randA(idMin, idMax));
        }
    }
}
