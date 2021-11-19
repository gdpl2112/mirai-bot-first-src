package Entitys;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Data {
    private String header;
    private int len;
    private byte[] bytes;
    private String token;
    private int id;

    public Data() {
    }

    public Data(String header, int len, byte[] bytes, String token, int id) {
        this.header = header;
        this.len = len;
        this.bytes = bytes;
        this.token = token;
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public Data setHeader(String header) {
        this.header = header;
        return this;
    }

    public int getLen() {
        return len;
    }

    public Data setLen(int len) {
        this.len = len;
        return this;
    }

    public String toByteString(){
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public byte[] getBytes() {
        return bytes;
    }

    public Data setBytes(byte[] bytes) {
        this.bytes = bytes;
        return this;
    }

    public String getToken() {
        return token;
    }

    public Data setToken(String token) {
        this.token = token;
        return this;
    }

    public int getId() {
        return id;
    }

    public Data setId(int id) {
        this.id = id;
        return this;
    }

    @Override
    public String toString() {
        return "Data{" +
                "header='" + header + '\'' +
                ", len=" + len +
                ", token='" + token + '\'' +
                ", id=" + id +
                ", bytes=" + Arrays.toString(bytes) +
                '}';
    }
}
