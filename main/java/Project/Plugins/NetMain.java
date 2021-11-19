package Project.Plugins;

public class NetMain {
    public static final String rootPath = "http://123.57.42.227:20041";

    public static final String mediaUrl = "/api/search/video?keyword=%s&type=%s";

    public static final String getPicNoNum = "/api/search/pic?keyword=%s";

    public static final String getPicNum = "/api/search/pic?keyword=%s&num=%s";

    public static final String getPicNoNumDt = "/api/search/pic?keyword=%s&type=duit";

    public static final String parsePic = "/api/search/parseImgs?url=%s&type=%s";

    public static final String getSongType = "/api/search/song?keyword=%s&type=%s";

    public static final String getWeatherShort = "/api/shortWeather?address=%s";

    public static final String getWeatherDetail = "/api/weather?address=%s";
}
