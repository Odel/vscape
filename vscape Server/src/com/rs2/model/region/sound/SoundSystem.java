package com.rs2.model.region.sound;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 7/7/12 Time: 8:34 PM To change
 * this template use File | Settings | File Templates.
 */
public class SoundSystem {
	public enum SoundData {
		WHIP(1080, 1658, SoundType.ANIMATION);

		private int soundId;
		private int activateOnId;
		private SoundType soundType;

		SoundData(int soundId, int activateOnId, SoundType soundType) {
			this.soundId = soundId;
			this.activateOnId = activateOnId;
			this.soundType = soundType;
		}

		public static SoundData forId(int id, SoundType type) {
			for (SoundData soundData : SoundData.values())
				if (soundData.activateOnId == id && type == soundData.soundType)
					return soundData;
			return null;
		}

		public int getSoundId() {
			return soundId;
		}
	}

	public enum SoundType {
		ANIMATION, GRAPHIC
	}
}
