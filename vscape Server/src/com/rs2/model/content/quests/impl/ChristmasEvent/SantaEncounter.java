package com.rs2.model.content.quests.impl.ChristmasEvent;

import com.rs2.model.Position;
import com.rs2.model.World;
import static com.rs2.model.content.dialogue.Dialogues.LAUGHING;
import static com.rs2.model.content.quests.impl.ChristmasEvent.ChristmasEvent.ENCOUNTER;
import static com.rs2.model.content.quests.impl.ChristmasEvent.ChristmasEvent.ICE_FIEND;
import static com.rs2.model.content.quests.impl.ChristmasEvent.ChristmasEvent.ICE_GIANT;
import static com.rs2.model.content.quests.impl.ChristmasEvent.ChristmasEvent.ICE_SPIDER;
import static com.rs2.model.content.quests.impl.ChristmasEvent.ChristmasEvent.ICE_TROLL;
import static com.rs2.model.content.quests.impl.ChristmasEvent.ChristmasEvent.ICE_WOLF;
import static com.rs2.model.content.quests.impl.ChristmasEvent.ChristmasEvent.LIGHT_CREATURE;
import static com.rs2.model.content.quests.impl.ChristmasEvent.ChristmasEvent.SANTA;
import static com.rs2.model.content.quests.impl.ChristmasEvent.ChristmasEvent.SNOWY_JAIL;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.Player;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;
import java.util.ArrayList;


public class SantaEncounter {
    public final Npc santa = new Npc(SANTA);
    public ArrayList<Npc> lightCreatures = new ArrayList<>();
    public int heightLevel;
    public final Player player;
    public boolean encounterRunning = false;
    public boolean snowballTimerRunning = false;
    public boolean snowballsReady = false;
    public SantaEncounter(final Player player) {
	this.player = player;
    }
    
    public enum LightCreatureEnum {
	ONE(0, new Position(2782, 3868, 0), new Position(2784, 3868, 0)),
	TWO(1, new Position(2781, 3867, 0), new Position(2785, 3867, 0)),
	THREE(2, new Position(2780, 3866, 0), new Position(2786, 3866, 0)),
	FOUR(3, new Position(2765, 3846, 0), new Position(2775, 3846, 0)),
	FIVE(4, new Position(2769, 3852, 0), new Position(2775, 3847, 0)),
	SIX(5, new Position(2775, 3855, 0), new Position(2779, 3855, 0)),
	SEVEN(6, new Position(2762, 3856, 0), new Position(2771, 3856, 0)),
	EIGHT(7, new Position(2765, 3851, 0), new Position(2762, 3854, 0)),
	NINE(8, new Position(2766, 3849, 0), new Position(2765, 3846, 0)),
	TEN(9, new Position(2758, 3848, 0), new Position(2758, 3861, 0)),
	ELEVEN(10, new Position(2781, 3843, 0), new Position(2787, 3843, 0)),
	TWELVE(11, new Position(2797, 3837, 0), new Position(2782, 3837, 0)),
	THIRTEEN(12, new Position(2787, 3844, 0), new Position(2779, 3847, 0)),
	FOURTEEN(13, new Position(2778, 3849, 0), new Position(2781, 3852, 0)),
	FIFTEEN(14, new Position(2783, 3854, 0), new Position(2789, 3846, 0)),
	SIXTEEN(15, new Position(2791, 3854, 0), new Position(2792, 3851, 0)),
	SEVENTEEN(16, new Position(2792, 3856, 0), new Position(2799, 3856, 0)),
	EIGHTEEN(17, new Position(2801, 3853, 0), new Position(2804, 3848, 0)),
	NINETEEN(18, new Position(2802, 3846, 0), new Position(2799, 3846, 0)),
	TWENTY(19, new Position(2805, 3848, 0), new Position(2808, 3852, 0)),
	TWENTY1(20, new Position(2808, 3857, 0), new Position(2803, 3855, 0)),
	TWENTY2(21, new Position(2793, 3840, 0), new Position(2797, 3843, 0)),
	TWENTY3(22, new Position(2791, 3845, 0), new Position(2795, 3845, 0)),
	TWENTY4(23, new Position(2805, 3844, 0), new Position(2807, 3838, 0)),
	TWENTY5(24, new Position(2782, 3871, 0), new Position(2782, 3872, 0)),
	TWENTY6(25, new Position(2784, 3871, 0), new Position(2784, 3872, 0));
	public int id;
	public Position startPos;
	public Position endPos;
	
	LightCreatureEnum(int id, Position startPos, Position endPos) {
	    this.id = id;
	    this.startPos = startPos;
	    this.endPos = endPos;
	}
	
	public static LightCreatureEnum forIndex(int index) {
	    for(LightCreatureEnum l : LightCreatureEnum.values()) {
		if(index == l.id) {
		    return l;
		}
	    }
	    return null;
	}
	
	public Position getStartPosition() {
	    return this.startPos;
	}
	
	public Position getEndPosition() {
	    return this.endPos;
	}
    }
    public void sendDelaySantaChat(final String[] chat) {
	CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
	    @Override
	    public void execute(CycleEventContainer b) {
		b.stop();
	    }

	    @Override
	    public void stop() {
		player.getDialogue().setLastNpcTalk(SANTA);
		player.getDialogue().sendNpcChat(chat, LAUGHING);
		player.getDialogue().endDialogue();
	    }
	}, 3);
    }
    public void spawnEncounterNpc(Npc npc, Position spawningPosition) {
	npc.setPosition(spawningPosition);
	npc.setSpawnPosition(spawningPosition);
	if(npc.getNpcId() == ChristmasEvent.SANTA) {
	    npc.setWalkType(Npc.WalkType.STAND);
	} else {
	    npc.setWalkType(Npc.WalkType.WALK);
	    npc.setMaxWalk(new Position(spawningPosition.getX() + 5, spawningPosition.getY() + 5, heightLevel));
	    npc.setMinWalk(new Position(spawningPosition.getX() - 5, spawningPosition.getY() - 5, heightLevel));
	}
	npc.setCurrentX(spawningPosition.getX());
	npc.setCurrentY(spawningPosition.getY());
	npc.setNeedsRespawn(false);
	World.register(npc);
    }
    public void startEncounterLogic() {
	for(int i = 0; i < LightCreatureEnum.values().length; i++) {
	    lightCreatures.add(new Npc(LIGHT_CREATURE));
	}
	heightLevel = player.getPosition().getZ();
	final int combat = player.getCombatLevel();
	final int spawnId = combat < 10 ? ICE_FIEND : combat < 20 ? ICE_FIEND : combat < 40 ? ICE_GIANT : combat < 70 ? ICE_SPIDER : combat < 110 ? ICE_WOLF : ICE_TROLL;
	spawnEncounterNpc(santa, new Position(2783, 3869, heightLevel));
	for(int i = 0; i < LightCreatureEnum.values().length; i++) {
	    LightCreatureEnum l = LightCreatureEnum.forIndex(i);
	    if (l != null && lightCreatures.get(i) != null) {
		spawnEncounterNpc(lightCreatures.get(i), new Position(l.getStartPosition().getX(), l.getStartPosition().getY(), heightLevel));
	    }
	}
	CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
	    boolean enemySpawned = false;
	    @Override
	    public void execute(CycleEventContainer b) {
		if (player == null || santa == null || !santa.isVisible() || santa.isDead()) {
		    b.stop();
		    return;
		} else if (!player.Area(2754, 2814, 3833, 3873) || !encounterRunning || player.getPosition().getZ() != heightLevel) {
		    b.stop();
		    return;
		} else {
		    if (Misc.goodDistance(player.getPosition(), lightCreatures.get(2).getPosition(), 1) || Misc.goodDistance(player.getPosition(), lightCreatures.get(1).getPosition(), 1) || Misc.goodDistance(player.getPosition(), lightCreatures.get(0).getPosition(), 1)) {
			player.teleport(new Position(ENCOUNTER.getX(), ENCOUNTER.getY(), heightLevel));
			sendDelaySantaChat(new String[]{"HO HO! You thought you could stop me?!"});
		    }
		    if (Misc.random(20) == 1 && !enemySpawned && !Misc.goodDistance(player.getPosition(), santa.getPosition(), 4)) {
			enemySpawned = true;
			Npc spawn = new Npc(spawnId);
			spawn.setCombatDelay(3);
			NpcLoader.spawnNpc(player, spawn, true, false);
			player.getDialogue().setLastNpcTalk(SANTA);
			player.getDialogue().sendNpcChat("Let's see how you like this!", LAUGHING);
			player.getDialogue().endDialogue();
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			    @Override
			    public void execute(CycleEventContainer b) {
				b.stop();
			    }

			    @Override
			    public void stop() {
				enemySpawned = false;
			    }
			}, 15);
		    }
		    for (int i = 0; i < lightCreatures.size(); i++) {
			LightCreatureEnum l = LightCreatureEnum.forIndex(i);
			if (l != null && lightCreatures.get(i) != null) {
			    if (lightCreatures.get(i).getPosition().equals(new Position(l.getStartPosition().getX(), l.getStartPosition().getY(), heightLevel)) && lightCreatures.get(i).getFrozenImmunity().completed()) {
				if(lightCreatures.get(i).getMovementHandler().canWalk()) {
				lightCreatures.get(i).walkTo(new Position(l.getEndPosition().getX(), l.getEndPosition().getY(), heightLevel), true);
				}
			    }
			    if ((lightCreatures.get(i).getPosition().equals(new Position(l.getEndPosition().getX(), l.getEndPosition().getY(), heightLevel))) && lightCreatures.get(i).getFrozenImmunity().completed()) {
				if(lightCreatures.get(i).getMovementHandler().canWalk()) {
				lightCreatures.get(i).walkTo(new Position(l.getStartPosition().getX(), l.getStartPosition().getY(), heightLevel), true);
				}
			    }
			    if (!lightCreatures.get(i).getPosition().equals(new Position(l.getStartPosition().getX(), l.getStartPosition().getY(), heightLevel)) && !lightCreatures.get(i).getPosition().equals(new Position(l.getEndPosition().getX(), l.getEndPosition().getY(), heightLevel)) && !lightCreatures.get(i).isMoving()) {
				if(lightCreatures.get(i).getMovementHandler().canWalk()) {
				lightCreatures.get(i).walkTo(new Position(l.getStartPosition().getX(), l.getStartPosition().getY(), heightLevel), true);
				}
			    }
			    if (Misc.goodDistance(player.getPosition(), lightCreatures.get(i).getPosition(), 1)) {
				player.teleport(new Position(ENCOUNTER.getX(), ENCOUNTER.getY(), heightLevel));
				sendDelaySantaChat(new String[]{"Ho ho ho! Feel the power of my magic!"});
			    }
			}
		    }
		}
	    }
	    @Override
	    public void stop() {
		if(player != null) {
		    encounterRunning = false;
		    if(!player.getPosition().equals(new Position(2755, 3649, 0)) && !Misc.goodDistance(player.getPosition(), santa.getPosition(), 1)) {
			player.teleport(SNOWY_JAIL);
		    }
		}
		
		for(Npc npc : World.getNpcs()) {
		    if(npc != null && npc.Area(2754, 2814, 3833, 3873) && npc.getPosition().getZ() == heightLevel) {
			NpcLoader.destroyNpc(npc);
		    }
		}
	    }
	}, 2);
    }
    public void startEncounter() {
	player.fadeTeleport(new Position(ENCOUNTER.getX(), ENCOUNTER.getY(), player.getIndex() * 4));
	player.setStopPacket(true);
	encounterRunning = true;
	new GameObject(5015, 2773, 3835, 0, 0, 10, 0, 999999, true);
	new GameObject(5015, 2772, 3835, 0, 0, 10, 0, 999999, true);
	CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
	    @Override
	    public void execute(CycleEventContainer b) {
		sendDelaySantaChat(new String[] {"HO HO! You think you can best me?", "Just try and reach me! Ho ho ho!"});
		b.stop();
	    }

	    @Override
	    public void stop() {
		player.setStopPacket(false);
		player.getSantaEncounter().startEncounterLogic();
	    }
	}, 5);
	
    }
    public void startSnowballTimer() {
	if (!snowballTimerRunning) {
	    snowballTimerRunning = true;
	    CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
		@Override
		public void execute(CycleEventContainer b) {
		    b.stop();
		}

		@Override
		public void stop() {
		    snowballsReady = true;
		    snowballTimerRunning = false;
		}
	    }, 300);
	}
    }
}
