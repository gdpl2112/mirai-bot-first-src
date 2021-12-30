package temp;

import net.mamoe.mirai.message.data.Audio;

import static Project.Tools.Tool.randA;

public class M2 {
    public static void main(String[] args) {
        int idMin = 501,idMax=521;
        for (int i = 0; i < 1000; i++) {
            System.out.println(randA(idMin, idMax));
        }
    }
}
