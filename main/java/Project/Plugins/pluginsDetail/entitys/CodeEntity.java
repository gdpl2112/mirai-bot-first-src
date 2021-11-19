package Project.Plugins.pluginsDetail.entitys;

public class CodeEntity {
	private String stdin;
	private CodeContent[] files;
	private String command;

	public String getStdin(){
		return this.stdin;
	}

	public CodeEntity setStdin(String stdin) {
		this.stdin = stdin;
		return this;
	}

	public CodeContent[] getFiles(){
		return this.files;
	}

	public CodeEntity setFiles(CodeContent[] files) {
		this.files = files;
		return this;
	}

	public String getCommand(){
		return this.command;
	}

	public CodeEntity setCommand(String command) {
		this.command = command;
		return this;
	}
}