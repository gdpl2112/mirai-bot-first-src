package Project.Plugins.pluginsDetail;

public class RunAll {
  /*  public static final String baseUrl = "https://glot.io/run/%s?version=latest";
    public static final String runJava = String.format(baseUrl, "java");
    public static final String runC = String.format(baseUrl, "c");
    public static final String runPython = String.format(baseUrl, "python");

    public static CodeResponse runJava(CodeEntity entity) throws IOException {
        Connection connection = Jsoup.connect(runJava)
                .ignoreContentType(true)
                .userAgent("Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.25 Mobile Safari/537.36");
        connection.requestBody(JSON.toJSONString(entity));
        Document document = connection.post();
        String jsonStr = document.body().text();
        CodeResponse response = JSON.parseObject(jsonStr, CodeResponse.class);
        return response;
    }

    public static CodeResponse runC(CodeEntity entity) throws IOException {
        Connection connection = Jsoup.connect(runC)
                .ignoreContentType(true)
                .userAgent("Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.25 Mobile Safari/537.36");
        connection.requestBody(JSON.toJSONString(entity));
        Document document = connection.post();
        String jsonStr = document.body().text();
        CodeResponse response = JSON.parseObject(jsonStr, CodeResponse.class);
        return response;
    }

    public static CodeResponse runPython(CodeEntity entity) throws IOException {
        Connection connection = Jsoup.connect(runPython)
                .ignoreContentType(true)
                .userAgent("Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.25 Mobile Safari/537.36");
        connection.requestBody(JSON.toJSONString(entity));
        Document document = connection.post();
        String jsonStr = document.body().text();
        CodeResponse response = JSON.parseObject(jsonStr, CodeResponse.class);
        return response;
    }

    public static CodeResponse runAny(CodeEntity entity, String language) throws IOException {
        Connection connection = Jsoup.connect(String.format(baseUrl, language))
                .ignoreContentType(true)
                .userAgent("Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.25 Mobile Safari/537.36");
        connection.requestBody(JSON.toJSONString(entity));
        Document document = connection.post();
        String jsonStr = document.body().text();
        CodeResponse response = JSON.parseObject(jsonStr, CodeResponse.class);
        return response;
    }


    public static void main(String[] args) throws Exception {
        CodeEntity entity = new CodeEntity();
        CodeContent content = new CodeContent();
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入文件名:");
        String fileName = scanner.nextLine();
        content.setName(fileName);
        StringBuilder sb = new StringBuilder();
        String line = null;
        System.out.println("开始输入文本内容 已单行ok结束:");
        while (!(line = scanner.nextLine()).equals("ok")) {
            sb.append(line).append("\n");
        }
        content.setContent(sb.toString());
        entity.setCommand("");
        entity.setStdin("");
        entity.setFiles(new CodeContent[]{content});
        System.out.println("正在运行....");
        CodeResponse response = new CodeResponse();
        if (fileName.endsWith("java"))
            response = runJava(entity);
        else if (fileName.endsWith("c"))
            response = runC(entity);
        else if (fileName.endsWith("py"))
            response = runPython(entity);
        System.out.println("运行输出:" + response.getStdout());
        System.out.println("运行警告输出:" + response.getStderr());
        System.out.println("运行错误输出:" + response.getError());
    }*/
}
