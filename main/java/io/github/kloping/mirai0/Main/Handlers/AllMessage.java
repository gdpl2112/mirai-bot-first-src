package io.github.kloping.mirai0.Main.Handlers;

/**
 * @author github-kloping
 */
public class AllMessage {
	private Number[] internalIds;
	private Number senderId;
	private Number[] ids;
	private Number botId;
	private String type;
	private Number fromId;
	private String content;

	public Number[] getInternalIds(){
		return this.internalIds;
	}

	public AllMessage setInternalIds(Number[] internalIds) {
		this.internalIds = internalIds;
		return this;
	}

	public Number getSenderId(){
		return this.senderId;
	}

	public AllMessage setSenderId(Number senderId) {
		this.senderId = senderId;
		return this;
	}

	public Number[] getIds(){
		return this.ids;
	}

	public AllMessage setIds(Number[] ids) {
		this.ids = ids;
		return this;
	}

	public Number getBotId(){
		return this.botId;
	}

	public AllMessage setBotId(Number botId) {
		this.botId = botId;
		return this;
	}

	public String getType(){
		return this.type;
	}

	public AllMessage setType(String type) {
		this.type = type;
		return this;
	}

	public Number getFromId(){
		return this.fromId;
	}

	public AllMessage setFromId(Number fromId) {
		this.fromId = fromId;
		return this;
	}

	public String getContent(){
		return this.content;
	}

	public AllMessage setContent(String content) {
		this.content = content;
		return this;
	}
}