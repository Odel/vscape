package com.rs2.model.content.skills;

import com.rs2.Constants;
import com.rs2.model.players.Player;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class SkillcapeEmotes {
	private Player player;

	public SkillcapeEmotes(Player player) {
		this.player = player;
	}
	
	public enum CapeEmote
	{
		ATTACK(9747, 9748, 4959, 823, 6),
		STRENGTH(9750, 9751, 4981, 828, 17),
		DEFENCE(9753, 9754, 4961, 824, 8);
		
		private int skillcapeId;
		private int skillcapeTrimmedId;
		private int animId;
		private int graphicId;
		private int time;
		
		CapeEmote(int skillcapeId, int skillcapeTrimmedId, int animId, int graphicId, int time) {
			this.skillcapeId = skillcapeId;
			this.skillcapeTrimmedId = skillcapeTrimmedId;
			this.animId = animId;
			this.graphicId = graphicId;
			this.time = time;
		}
		
		
		public static CapeEmote forId(int skillcapeId) {
			for (CapeEmote emote : CapeEmote.values())
					if (skillcapeId == emote.skillcapeId || skillcapeId == emote.skillcapeTrimmedId)
						return emote;
			return null;
		}
	}
	
	public void performEmote(int emoteId, int graphicId, int time) {
		player.getUpdateFlags().sendAnimation(emoteId, 0);
		player.getUpdateFlags().sendGraphic(graphicId);
		player.setStopPacket(true);
        CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
            	player.resetAnimation();
            	player.setStopPacket(false);
                container.stop();
            }
            @Override
            public void stop(){
            }
        }, time);
	}
	
	public boolean doEmote(int buttonId) {
		if(buttonId == 72254)
		{
			int capeId = player.getEquipment().getId(Constants.CAPE);
			CapeEmote emote = CapeEmote.forId(capeId);
			if(emote == null)
			{
				player.getActionSender().sendMessage("You're not wearing a skillcape.");
				return false;
			}
			performEmote(emote.animId,emote.graphicId,emote.time);
			return true;
		}
		return false;
	}

}
