package fr.satiscraftoryteam.satiscraftory.utils;

import fr.satiscraftoryteam.satiscraftory.common.block.base.properties.Attribute;
import fr.satiscraftoryteam.satiscraftory.common.block.base.properties.attributes.FacingAttribute;
import fr.satiscraftoryteam.satiscraftory.common.block.base.properties.StateAttribute;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BlockstateUtils {


    public static BlockState getStateForPlacement(Block block, @Nullable BlockState state, BlockPlaceContext context) {
        return getStateForPlacement(block, state, context.getLevel(), context.getClickedPos(), context.getPlayer(), context.getClickedFace());
    }

    public static BlockState getStateForPlacement(Block block, @Nullable BlockState state, @NotNull LevelAccessor world, @NotNull BlockPos pos, @Nullable Player player, @NotNull Direction face) {
        if (state == null) {
            return null;
        }
        for (Attribute attr : Attribute.getAll(block)) {
            if (attr instanceof FacingAttribute atr) {
                state = atr.getStateForPlacement(block, state, world, pos, player, face);
            }
        }
        return state;
    }

    public static void fillBlockStateContainer(Block block, StateDefinition.Builder<Block, BlockState> builder) {
        List<Property<?>> properties = new ArrayList<>();
        for (Attribute attr : Attribute.getAll(block)) {
            if (attr instanceof StateAttribute atr) {
                atr.fillBlockStateContainer(block, properties);
            }
        }
        if (!properties.isEmpty()) {
            builder.add(properties.toArray(new Property[0]));
        }
    }
}
