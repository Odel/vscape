package com.rs2.model.content.tutorialisland;

import java.util.HashMap;
import java.util.Map;

import com.rs2.model.Position;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.players.Player;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 5/5/12 Time: 12:18 AM To change
 * this template use File | Settings | File Templates.
 */
public enum TutorialObjects {
	DOOR1(4, new int[]{3014}, new Position[]{new Position(3098, 3107)}) {
		public void applyObjectClicking(Player player) {
			player.getNewComersSide().setTutorialIslandStage(player.getNewComersSide().getTutorialIslandStage() + 1, true);
			player.getActionSender().walkTo(1, 0, true);
			player.getActionSender().walkThroughDoor(getObjectId()[0], getObjectPosition()[0].getX(), getObjectPosition()[0].getY(), player.getPosition().getZ());
		}
	},
	GATE(14, new int[]{3015, 3016}, new Position[]{new Position(3089, 3092), new Position(3089, 3091)}) {
		public void applyObjectClicking(Player player) {
			player.getNewComersSide().setTutorialIslandStage(player.getNewComersSide().getTutorialIslandStage() + 1, true);
			player.getActionSender().walkTo(-1, 0, true);
			player.getActionSender().walkThroughTutIsGate(getObjectId()[0], getObjectId()[1], getObjectPosition()[0].getX(), getObjectPosition()[0].getY(), getObjectPosition()[0].getX(), getObjectPosition()[0].getY() - 1, 0);
		}
	},
	DOOR2(15, new int[]{3017}, new Position[]{new Position(3079, 3084)}) {
		public void applyObjectClicking(Player player) {
			player.getNewComersSide().setTutorialIslandStage(player.getNewComersSide().getTutorialIslandStage() + 1, true);
			player.getActionSender().walkTo(-1, 0, true);
			player.getActionSender().walkThroughDoor2(3017, 3079, 3084, 0);
		}
	},
	DOOR3(20, new int[]{3018}, new Position[]{new Position(3072, 3090)}) {
		public void applyObjectClicking(Player player) {
			player.getNewComersSide().setTutorialIslandStage(player.getNewComersSide().getTutorialIslandStage() + 1, true);
			player.getActionSender().walkTo(-1, 0, true);
			player.getActionSender().walkThroughDoor3(3018, 3072, 3090, 0);
		}
	},
	DOOR4(23, new int[]{3019}, new Position[]{new Position(3086, 3126)}) {
		public void applyObjectClicking(Player player) {
			player.getNewComersSide().setTutorialIslandStage(player.getNewComersSide().getTutorialIslandStage() + 1, true);
			player.getActionSender().walkTo(0, -1, true);
			player.getActionSender().walkThroughDoor(3019, 3086, 3126, 0);
		}
	},
	LADDER1(27, new int[]{3029}, new Position[]{new Position(3088, 3119)}) {
		public void applyObjectClicking(Player player) {
			player.getNewComersSide().setTutorialIslandStage(player.getNewComersSide().getTutorialIslandStage() + 1, true);
			Ladders.climbLadder(player, new Position(3088, 9520, 0));
		}
	},
	GATE1(37, new int[]{3021, 3020}, new Position[]{new Position(3094, 9502), new Position(3094, 9503)}) {
		public void applyObjectClicking(Player player) {
			player.getNewComersSide().setTutorialIslandStage(player.getNewComersSide().getTutorialIslandStage() + 1, true);
			player.getActionSender().walkTo(1, 0, true);
			player.getActionSender().walkThroughDoor2(3021, 3020, 3094, 9502, 3094, 9503, 0);
		}
	},
	GATE2(44, new int[]{3022, 3023}, new Position[]{new Position(3111, 9518), new Position(3111, 9519)}) {
		public void applyObjectClicking(Player player) {
			player.getNewComersSide().setTutorialIslandStage(player.getNewComersSide().getTutorialIslandStage() + 1, true);
			player.getActionSender().walkTo(player.getPosition().getX() < 3111 ? 1 : -1, 0, true);
			player.getActionSender().walkThroughDoor3(3022, 3023, 3111, 9518, 3111, 9519, 0);
		}
	},
	GATE3(45, new int[]{3022, 3023}, new Position[]{new Position(3111, 9518), new Position(3111, 9519)}) {
		public void applyObjectClicking(Player player) {
			player.getActionSender().walkTo(player.getPosition().getX() < 3111 ? 1 : -1, 0, true);
			player.getActionSender().walkThroughDoor3(3022, 3023, 3111, 9518, 3111, 9519, 0);
		}
	},
	GATE4(46, new int[]{3022, 3023}, new Position[]{new Position(3111, 9518), new Position(3111, 9519)}) {
		public void applyObjectClicking(Player player) {
			player.getActionSender().walkTo(player.getPosition().getX() < 3111 ? 1 : -1, 0, true);
			player.getActionSender().walkThroughDoor3(3022, 3023, 3111, 9518, 3111, 9519, 0);
		}
	},
	GATE6(48, new int[]{3022, 3023}, new Position[]{new Position(3111, 9518), new Position(3111, 9519)}) {
		public void applyObjectClicking(Player player) {
			player.getActionSender().walkTo(player.getPosition().getX() < 3111 ? 1 : -1, 0, true);
			player.getActionSender().walkThroughDoor3(3022, 3023, 3111, 9518, 3111, 9519, 0);
		}
	},
	LADDER2(48, new int[]{3030}, new Position[]{new Position(3111, 9526)}) {
		public void applyObjectClicking(Player player) {
			player.getNewComersSide().setTutorialIslandStage(player.getNewComersSide().getTutorialIslandStage() + 1, true);
			Ladders.climbLadder(player, new Position(3111, 3125, 0));
		}
	},
	BANK(49, new int[]{3045}, new Position[]{new Position(3122, 3124)}) {
		public void applyObjectClicking(Player player) {
			Dialogues.startDialogue(player, 953);
		}
	},
	DOOR5(50, new int[]{3024}, new Position[]{new Position(3125, 3124)}) {
		public void applyObjectClicking(Player player) {
			player.getNewComersSide().setTutorialIslandStage(player.getNewComersSide().getTutorialIslandStage() + 1, true);
			player.getActionSender().walkTo(1, 0, true);
			player.getActionSender().walkThroughDoor(3024, 3125, 3124, 0);
		}
	},
	DOOR6(52, new int[]{3025}, new Position[]{new Position(3130, 3124)}) {
		public void applyObjectClicking(Player player) {
			player.getNewComersSide().setTutorialIslandStage(player.getNewComersSide().getTutorialIslandStage() + 1, true);
			player.getActionSender().walkTo(1, 0, true);
			player.getActionSender().walkThroughDoor(3025, 3130, 3124, 0);
		}
	},
	DOOR7(59, new int[]{3026}, new Position[]{new Position(3122, 3102)}) {
		public void applyObjectClicking(Player player) {
			player.getNewComersSide().setTutorialIslandStage(player.getNewComersSide().getTutorialIslandStage() + 1, true);
			player.getActionSender().walkTo(0, -1, true);
			player.getActionSender().walkThroughDoor(3026, 3122, 3102, 0);
		}
	},
	;
	private int stageIndex;
	private int[] objectId;
	private Position[] objectPosition;

	public abstract void applyObjectClicking(Player player);

	static Map<Integer, TutorialObjects> stages = new HashMap<Integer, TutorialObjects>();

	static {
		for (TutorialObjects data : values()) {
			stages.put(data.stageIndex, data);
		}
	}

	public static TutorialObjects forId(int stageId) {
		return stages.get(stageId);
	}

	TutorialObjects(int stageIndex, int[] objectId, Position[] objectPosition) {
		this.stageIndex = stageIndex;
		this.objectId = objectId;
		this.objectPosition = objectPosition;
	}

	public int getStageIndex() {
		return stageIndex;
	}

	public int[] getObjectId() {
		return objectId;
	}

	public Position[] getObjectPosition() {
		return objectPosition;
	}
}
