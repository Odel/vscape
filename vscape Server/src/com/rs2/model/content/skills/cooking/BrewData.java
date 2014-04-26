package com.rs2.model.content.skills.cooking;

import com.rs2.model.players.Player;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 23/12/11 Time: 23:48 To change
 * this template use File | Settings | File Templates.
 */
public class BrewData {

	@SuppressWarnings("unused")
	private Player player;
	public static final int BAD_ALE_OBJECT = 7439;
	@SuppressWarnings("unused")
	private static final int BAD_CIDER_OBJECT = 7440;
	@SuppressWarnings("unused")
	private static final int BAD_ALE_BARREL = 7408;
	@SuppressWarnings("unused")
	private static final int UNFERMENTED_BARREL = 7409;
	@SuppressWarnings("unused")
	private static final int BAD_CIDER_BARREL = 7410;

	private int aleRuns;
	@SuppressWarnings("unused")
	private int year;
	@SuppressWarnings("unused")
	private int month;
	@SuppressWarnings("unused")
	private int day;
	@SuppressWarnings("unused")
	private int hour;
	private int objectId;
	private int fermentOne;
	private int fermentTwo;
	private int finished;
	@SuppressWarnings("unused")
	private int mature;
	@SuppressWarnings("unused")
	private int fullBarrel;
	@SuppressWarnings("unused")
	private int matureBarrel;

	public BrewData(Player player) {
		this.player = player;
	}

	public int getAleRuns() {
		return aleRuns;
	}

	public void setAleRuns(int aleRuns) {
		this.aleRuns = aleRuns;
	}

	public void resetDatas() {
		this.objectId = 0;
		this.fermentOne = 0;
		this.fermentTwo = 0;
		this.finished = 0;
		this.mature = 0;
		this.fullBarrel = 0;
		this.matureBarrel = 0;
	}

	public void setDatas(int objectId, int fermentOne, int fermentTwo, int finished, int mature, int fullBarrel, int matureBarrel) {
		this.objectId = objectId;
		this.fermentOne = fermentOne;
		this.fermentTwo = fermentTwo;
		this.finished = finished;
		this.mature = mature;
		this.fullBarrel = fullBarrel;
		this.matureBarrel = matureBarrel;
	}

	public void sendFermentingState(String fermentingState) {
		if (objectId == -1 || fermentOne == -1 || fermentTwo == -1 || finished == -1)
			return;
		if (fermentingState == "fermentingOne") {
			//player.getActionSender().sendNewObject(BrewsFermenting.KELDAGRIM_VAT, fermentOne, 0, 10);
			//player.getActionSender().sendNewObject(BrewsFermenting.KELDAGRIM_BARREL, UNFERMENTED_BARREL, 0, 10);
		}
		if (fermentingState == "fermentingTwo") {
			//player.getActionSender().sendNewObject(BrewsFermenting.KELDAGRIM_VAT, fermentTwo, 0, 10);
			//player.getActionSender().sendNewObject(BrewsFermenting.KELDAGRIM_BARREL, UNFERMENTED_BARREL, 0, 10);
		}
		if (fermentingState == "finished") {
			//player.getActionSender().sendNewObject(BrewsFermenting.KELDAGRIM_VAT, finished, 0, 10);
			//player.getActionSender().sendNewObject(BrewsFermenting.KELDAGRIM_BARREL, fullBarrel, 0, 10);
		}
		if (fermentingState == "mature") {
			//player.getActionSender().sendNewObject(BrewsFermenting.KELDAGRIM_VAT, mature, 0, 10);
			//player.getActionSender().sendNewObject(BrewsFermenting.KELDAGRIM_BARREL, matureBarrel, 0, 10);
		}
		if (fermentingState == "bad") {
			if (finished == 7492) {
				//player.getActionSender().sendNewObject(BrewsFermenting.KELDAGRIM_VAT, BAD_ALE_OBJECT, 0, 10);
				//player.getActionSender().sendNewObject(BrewsFermenting.KELDAGRIM_BARREL, BAD_ALE_BARREL, 0, 10);
			} else {
				//player.getActionSender().sendNewObject(BrewsFermenting.KELDAGRIM_VAT, BAD_CIDER_OBJECT, 0, 10);
				//player.getActionSender().sendNewObject(BrewsFermenting.KELDAGRIM_BARREL, BAD_CIDER_BARREL, 0, 10);
			}
		}
	}

	public void setFermentingDate(int year, int month, int day, int hour) {
		this.year = year;
		this.month = month;
		this.day = day;
		this.hour = hour;

	}

	public void updateFermentingState() {
		/*if (year == 0 || month == 0 || day == 0 || hour == 0)
			return;
		if (year != Calendars.getYear() || month != Calendars.getMonth()) {
			sendFermentingState("bad");
			return;
		}
		if (Calendars.getDay() == day + 1 && (Calendars.HourOfYear() - hour) >= 23 && (Calendars.HourOfYear() - hour) <= 25) {
			sendFermentingState("finished");
		} else if (Calendars.HourOfYear() - hour <= 12) {
			sendFermentingState("fermentingOne");
		} else if (Calendars.HourOfYear() - hour > 12) {
			sendFermentingState("fermentingTwo");
		} else if (Calendars.HourOfYear() - hour == 26) {
			sendFermentingState("mature");
		} else if (Calendars.HourOfYear() - hour > 26) {
			sendFermentingState("bad");
		}*/
	}

}
