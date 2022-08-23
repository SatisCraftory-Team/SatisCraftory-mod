package fr.satiscraftoryteam.satiscraftory.common.block.base;

import com.google.common.collect.Lists;
import fr.satiscraftoryteam.satiscraftory.common.interfaces.IBlockProperties;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.MachineBaseTileEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collection;
import java.util.function.Consumer;

public interface Attribute {

    interface TileAttribute<TILE extends MachineBaseTileEntity> extends Attribute {}

    static boolean has(BlockState state, Class<? extends Attribute> type) {
        return has(state.getBlock(), type);
    }

    static boolean has(Block block, Class<? extends Attribute> type) {
        return block instanceof IBlockProperties typeBlock && typeBlock.has(type);
    }

    static <T extends Attribute> T get(BlockState state, Class<T> type) {
        return get(state.getBlock(), type);
    }

    static <T extends Attribute> T get(Block block, Class<T> type) {
        return block instanceof IBlockProperties typeBlock ? typeBlock.get(type) : null;
    }

    static boolean has(Block block1, Block block2, Class<? extends Attribute> type) {
        return has(block1, type) && has(block2, type);
    }

    static <T extends Attribute> void ifHas(Block block, Class<T> type, Consumer<T> run) {
        if (block instanceof IBlockProperties typeBlock) {
            T attribute = typeBlock.get(type);
            if (attribute != null) {
                run.accept(attribute);
            }
        }
    }
}
