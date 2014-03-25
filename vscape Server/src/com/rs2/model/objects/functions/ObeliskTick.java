package com.rs2.model.objects.functions;

import java.awt.Point;
import java.awt.Rectangle;

import com.rs2.model.Graphic;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.Player;
import com.rs2.model.tick.Tick;
import com.rs2.util.Area;
import com.rs2.util.Misc;

/**
 *
 */
public class ObeliskTick extends Tick {
    private static final int TICKS = (int) Misc.secondsToTicks(5);
    private static final int ACTIVE = 14825;
    @SuppressWarnings("unused")
	private static final int[] INACTIVE = { };

    private Obelisk obelisk;

    public ObeliskTick(Obelisk obelisk) {
        super(TICKS, true);
        this.obelisk = obelisk;
    }

    @Override
    public void execute() {
        if (!obelisk.active) {
            obelisk.active = true;
            activateObelisk(obelisk);
            return;
        }
        stop();
        obelisk.active = false;
        Obelisk to;
        while (true) {
            int random = Misc.getRandom().nextInt(Obelisk.values().length);
            if (Obelisk.values()[random] == obelisk)
                continue;
            to = Obelisk.values()[random];
            break;
        }
        for (Player player : World.getPlayers()) {
            if (player == null)
                continue;
            if (obelisk.getArea().contains(new Point(player.getPosition().getX(), player.getPosition().getY()))) {
            	teleport(player, to);
            }
        }
        Position[] teleArea = obelisk.getGfxArea().calculateAllPositions();
        for (Position position : teleArea) {
            World.createStaticGraphic(Graphic.lowGraphic(342), position);
        }
    }
    
    private void teleport(Player player, Obelisk to) {
        int deltaX = player.getPosition().getX()-obelisk.position.getX();
        int deltaY = player.getPosition().getY()-obelisk.position.getY();
        player.getTeleportation().teleportObelisk(to.position.getX()+deltaX, to.position.getY()+deltaY, to.position.getZ(), false, "Ancient magic teleports you somewhere in the wilderness");
    }
    
    private void activateObelisk(Obelisk obelisk) {
        for (Position pillar : obelisk.pillars) {
            new GameObject(ACTIVE, pillar.getX(), pillar.getY(), pillar.getZ(), 0, 10, obelisk.pillarId, TICKS+3);
        }
    }
    
    private static Position[] getObeliskPositions(Obelisk obelisk) {
        Position location = obelisk.position;
        int i = 0;
        Position[] locations = new Position[4];
        for (int xMod = 0; xMod <= 4; xMod+=4) {
            for (int yMod = 0; yMod <= 4; yMod+=4) {
                locations[i++] = new Position(location.getX()+xMod, location.getY()+yMod, location.getZ());
            }
        }
        return locations;
    }

    public static void main(String[] args) {
        getObeliskPositions(Obelisk.A);
    }

    public static boolean clickObelisk(int objectId) {
        for (Obelisk obelisk : Obelisk.values()) {
            if (obelisk.pillarId == objectId) {
                if (obelisk.active)
                    return true;
                World.submit(new ObeliskTick(obelisk));
                return true;
            }
        }
        return false;
    }

    public enum Obelisk {

        A(14829, new Position(3154, 3618)),
        B(14830, new Position(3225, 3665)),
        C(14827, new Position(3033, 3730)),
        D(14828, new Position(3104, 3792)),
        E(14826, new Position(2978, 3864)),
        F(14831, new Position(3305, 3914));
        
        Position position;
        boolean active;
        Rectangle area;
        Area gfxArea;
        Position[] pillars;
        int pillarId;
        Obelisk(int pillarId, Position position) {
            this.pillarId = pillarId;
            this.position = position;
            this.active = false;
            this.gfxArea = Area.areaFromCorner(new Position(position.getX()+1, position.getY()+1, position.getZ()), 2, 2);
            this.area = new Rectangle(position.getX(), position.getY(), 5, 5);
            this.pillars = getObeliskPositions(this);
        }
        
        public Rectangle getArea() {
            return area;
        }
    
        public Area getGfxArea() {
            return gfxArea;
        }
    }
    
}
