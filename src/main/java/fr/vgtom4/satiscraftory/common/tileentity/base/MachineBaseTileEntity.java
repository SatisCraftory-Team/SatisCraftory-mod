package fr.vgtom4.satiscraftory.common.tileentity.base;

import fr.vgtom4.satiscraftory.common.interfaces.IHasTileEntity;
import fr.vgtom4.satiscraftory.common.registry.TileEntityRegistryObject;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;

//TODO: implement here capabilities
public abstract class MachineBaseTileEntity extends TileEntityUpdatable {

    public final ArrayList<BlockPos> BOUNDING_BLOCKS_POS = Lists.newArrayList();

    public void onAdded() {
        System.out.println("added" + this.getClass());
    }


    public MachineBaseTileEntity(TileEntityRegistryObject<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static void tickClient(Level level, BlockPos pos, BlockState state, MachineBaseTileEntity tile) {

    }

    public static void tickServer(Level level, BlockPos pos, BlockState state, MachineBaseTileEntity tile) {

    }

}
