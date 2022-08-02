package fr.vgtom4.satiscraftory.common.tileentity.base;

import fr.vgtom4.satiscraftory.common.interfaces.IHasTileEntity;
import fr.vgtom4.satiscraftory.common.registry.TileEntityRegistryObject;
import fr.vgtom4.satiscraftory.utils.RelativeOrientationUtils;
import joptsimple.util.KeyValuePair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;

//TODO: implement here capabilities
public abstract class MachineBaseTileEntity extends TileEntityUpdatable {

    public final ArrayList<Vec3i> BOUNDING_BLOCKS_POS = Lists.newArrayList();
    public final ArrayList<Tuple<Vec3i, RelativeOrientationUtils.RelativeOrientation>> CONVEYOR_INPUT_POS_ORIENTATION = Lists.newArrayList();
    public final ArrayList<Tuple<Vec3i, RelativeOrientationUtils.RelativeOrientation>> CONVEYOR_OUTPUT_POS_ORIENTATION = Lists.newArrayList();

    public void onAdded() {
        System.out.println("added" + this.getClass());
    }


    public MachineBaseTileEntity(TileEntityRegistryObject<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static void tickClient(Level level, BlockPos pos, BlockState state, MachineBaseTileEntity tile) {
        tile.onClientTick(level, pos, state, tile);
    }

    public static void tickServer(Level level, BlockPos pos, BlockState state, MachineBaseTileEntity tile) {
        tile.onServerTick(level, pos, state, tile);
    }

    public void onClientTick(Level level, BlockPos pos, BlockState state, MachineBaseTileEntity tile){

    }

    public void onServerTick(Level level, BlockPos pos, BlockState state, MachineBaseTileEntity tile){

    }

}
