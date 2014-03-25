package com.rs2.model.transport;

import com.rs2.model.Entity;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.players.MovementLock;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.Tick;

public class MagicCarpet {

    /**
     * Magic carpet ride
     * @param player the player
     * @param to the position to go to
     * @return if the player can travel or not
     */
    public static boolean ride(final Player player, final Position to) {
        final int cost = 200;
        Item gold = new Item(995, cost);
        if (!player.getInventory().playerHasItem(gold))
            return false;
        final int waitDuration = 2;
        player.getInventory().removeItem(gold);
        player.getMovementPaused().setWaitDuration(waitDuration);
        player.getMovementPaused().reset();
        player.getUpdateFlags().sendAnimation(2262);
        Tick t = new Tick(waitDuration) {
            @Override
            public void execute() {
                MovementLock lock = new MovementLock() {
                    @Override
                    public boolean forcesRun() {
                        return true;
                    }

                    @Override
                    public void start(Entity entity) {
                        player.setRunAnim(2261);
                        player.setWalkAnim(2261);
                        player.setAppearanceUpdateRequired(true);
                    }

                    @Override
                    public void end(Entity entity) {
                        player.setStandAnim(-1);
                        player.setRunAnim(-1);
                        player.setWalkAnim(-1);
                        player.getUpdateFlags().sendAnimation(2263);
                        player.setAppearanceUpdateRequired(true);
                        player.getMovementPaused().setWaitDuration(waitDuration);
                        player.getMovementPaused().reset();
                    }
                };
                player.walkTo(to, false);
                player.getMovementHandler().lock(lock);
                stop();
            }
        };
        World.getTickManager().submit(t);
        return true;
        
    }

}
