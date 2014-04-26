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
		DEFENCE(9753, 9754, 4961, 824, 8),
		RANGE(9756, 9757, 4973, 832, 9),
		PRAYER(9759, 9760, 4979, 829, 14),
		MAGIC(9762, 9763, 4939, 813, 6),
		RUNECRAFTING(9765, 9766, 4947, 817, 11),
		HITPOINT(9768, 9769, 4971, 833, 7),
		AGILITY(9771, 9772, 4977, 830, 8),
		HERBLORE(9774, 9775, 4969, 835, 15),
		THIEVING(9777, 9778, 4965, 826, 6),
		CRAFTING(9780, 9781, 4949, 818, 15),
		FLETCHING(9783, 9784, 4937, 812, 15),
		SLAYER(9786, 9787, 4967, 827, 5),
		CONSTRUCTION(9789, 9790, 4953, 820, 12),
		MINING(9792, 9793, 4941, 814, 9),
		SMITHING(9795, 9796, 4943, 815, 20),
		FISHING(9798, 9799, 4951, 819, 13),
		COOKING(9801, 9802, 4955, 821, 25),
		FIREMAKING(9804, 9805, 4975, 831, 9),
		WOODCUTTING(9807, 9808, 4957, 822, 22),
		FARMING(9810, 9811, 4963, 825, 13);
		
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
	
	public void performEmote(final int emoteId, final int graphicId, final int time) {
		player.setStopPacket(true);
		player.getMovementHandler().reset();
        CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
        		player.getUpdateFlags().sendAnimation(emoteId, 0);
        		player.getUpdateFlags().sendGraphic(graphicId);
                container.stop();
            }
            @Override
            public void stop(){
            }
        }, 2);
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
        }, time+1);
	}
	
	public boolean doEmote(int buttonId) {
		if(buttonId == 73123)
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
