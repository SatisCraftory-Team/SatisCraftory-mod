package fr.satiscraftoryteam.satiscraftory.common.block.buildings.production.miners;

import fr.satiscraftoryteam.satiscraftory.common.block.base.BlockProps;
import fr.satiscraftoryteam.satiscraftory.common.block.base.BoudingAttribute;
import fr.satiscraftoryteam.satiscraftory.common.block.base.MachineBaseBlock;
import fr.satiscraftoryteam.satiscraftory.common.block.base.ShapeAttribute;
import fr.satiscraftoryteam.satiscraftory.common.init.TileEntityInit;
import fr.satiscraftoryteam.satiscraftory.common.shapes.ShapesList;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.MinerMk1BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;

public class NewMinerMk1Block extends MachineBaseBlock {


    public NewMinerMk1Block() {
        super(BlockBehaviour.Properties.of(Material.METAL));

        blockProps.add(new ShapeAttribute(ShapesList.MINER_MK1));
        blockProps.add(new BoudingAttribute((pos, state, builder) -> {
            for (int x = -1; x <= 1; x++) {
                for (int y = 0; y <= 5; y++) {
                    for (int z = -1; z <= 3; z++) {
                        if (x != 0 || y != 0 || z != 0) {
                            builder.add(pos.offset(x, y, z));
                        }
                    }
                }
            }
        }));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new MinerMk1BlockEntity(blockPos, blockState);
    }

    @Override
    public BlockProps getType() {
        return super.blockProps;
    }
}
