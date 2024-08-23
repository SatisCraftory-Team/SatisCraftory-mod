package fr.satiscraftoryteam.satiscraftory.common.init;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.builder.TileEntityBuilder;
import fr.satiscraftoryteam.satiscraftory.common.registration.TileEntityDeferredHolder;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.ConveyorOutputPartBlockEntity;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.EliocubeBlockEntity;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.LogoBlockEntity;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.SmelterBlockEntity;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.TileEntityBoundingBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TileEntityInit {

    public static final TileEntityBuilder TILE_ENTITY_TYPES = new TileEntityBuilder(SatisCraftory.MODID);

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, SatisCraftory.MODID);

    public static final TileEntityDeferredHolder<LogoBlockEntity> LOGO_ENTITY = TILE_ENTITY_TYPES.builder(BlockInit.LOGO, LogoBlockEntity::new).build();
    public static final TileEntityDeferredHolder<EliocubeBlockEntity> ELIOCUBE_ENTITY = TILE_ENTITY_TYPES.builder(BlockInit.ELIOCUBE, EliocubeBlockEntity::new).build();
    public static final TileEntityDeferredHolder<TileEntityBoundingBlock> BOUNDING_BLOCK = TILE_ENTITY_TYPES.builder(BlockInit.BOUNDING_BLOCK, TileEntityBoundingBlock::new).build();
    public static final TileEntityDeferredHolder<ConveyorOutputPartBlockEntity> CONVEYOR_OUTPUT_PART_ENTITY = TILE_ENTITY_TYPES.builder(BlockInit.CONVEYOR_OUTPUT_PART, ConveyorOutputPartBlockEntity::new).build();
    public static final TileEntityDeferredHolder<ConveyorOutputPartBlockEntity> CONVEYOR_INPUT_PART_ENTITY = TILE_ENTITY_TYPES.builder(BlockInit.CONVEYOR_INPUT_PART, ConveyorOutputPartBlockEntity::new).build();
    public static final TileEntityDeferredHolder<SmelterBlockEntity> SMELTER_BLOCK_ENTITY = TILE_ENTITY_TYPES.builder(BlockInit.SMELTER, SmelterBlockEntity::new).build();
//    public static final TileEntityDeferredHolder<MinerMk1BlockEntity> MINER_MK1_BLOCK_ENTITY = TILE_ENTITY_TYPES.builder(BlockInit.MINER_MK1, MinerMk1BlockEntity::new).build();
//    public static final TileEntityDeferredHolder<ConveyorTileEntity> CONVEYOR_FULL = TILE_ENTITY_TYPES.register(BlockInit.CONVEYOR_FULL, (blockPos, blockState) -> new ConveyorTileEntity(blockPos,blockState,false));
//    public static final TileEntityDeferredHolder<ConveyorTileEntity> CONVEYOR = TILE_ENTITY_TYPES.register(BlockInit.CONVEYOR, ConveyorTileEntity::new);

}
