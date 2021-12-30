package Entitys.apiEntitys.pvpQQVoice;

import com.alibaba.fastjson.JSON;

import java.util.Arrays;

/**
 * @author github-kloping
 */
public class PvpQQVoice {
	private Yylb_34[] yylb_34;

	public Yylb_34[] getYylb_34(){
		return this.yylb_34;
	}

	public PvpQQVoice setYylb_34(Yylb_34[] yylb_34) {
		this.yylb_34 = yylb_34;
		return this;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}