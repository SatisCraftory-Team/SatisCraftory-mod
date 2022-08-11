package fr.satiscraftoryteam.satiscraftory.utils;

import net.minecraft.core.Direction;
import org.apache.commons.lang3.NotImplementedException;

//TODO: implement up and down
public class RelativeOrientationUtils {
    public enum RelativeOrientation {
        FRONT,
        BACK,
        RIGHT,
        LEFT
    }

    public static Direction getAbsoluteDirection(RelativeOrientation relativeOrientation, Direction referenceDirection){
        switch(relativeOrientation){
            case FRONT:
                return referenceDirection;
            case BACK:
                return referenceDirection.getOpposite();
            case RIGHT:
                return referenceDirection.getClockWise();
            case LEFT:
                return referenceDirection.getCounterClockWise();
            default:
                throw new NotImplementedException("up and down are not implemented");
        }
    }
}
