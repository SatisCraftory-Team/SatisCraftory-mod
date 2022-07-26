package fr.vgtom4.satiscraftory.common.init;

import fr.vgtom4.satiscraftory.SatisCraftory;
import fr.vgtom4.satiscraftory.common.tileentity.ConveyorInputPartBlockEntity;
import fr.vgtom4.satiscraftory.common.tileentity.ConveyorOutputPartBlockEntity;
import fr.vgtom4.satiscraftory.common.tileentity.EliocubeBlockEntity;
import fr.vgtom4.satiscraftory.common.tileentity.MinerMk1BlockEntity;
import fr.vgtom4.satiscraftory.common.builder.TileEntityBuilder;
import fr.vgtom4.satiscraftory.common.registry.TileEntityRegistryObject;
import fr.vgtom4.satiscraftory.common.tileentity.base.TileEntityBoundingBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TileEntityInit {

    public static final TileEntityBuilder TILE_ENTITY_TYPES = new TileEntityBuilder(SatisCraftory.MODID);

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SatisCraftory.MODID);



    public static final RegistryObject<BlockEntityType<EliocubeBlockEntity>> ELIOCUBE_ENTITY = BLOCK_ENTITIES.register("eliocube_entity",
            () -> BlockEntityType.Builder.of(EliocubeBlockEntity::new,
                    BlockInit.ELIOCUBE.get()).build(null));

    public static final RegistryObject<BlockEntityType<ConveyorInputPartBlockEntity>> CONVEYOR_INPUT_PART_ENTITY = BLOCK_ENTITIES.register("conveyor_input_part_entity",
            () -> BlockEntityType.Builder.of(ConveyorInputPartBlockEntity::new,
                    BlockInit.CONVEYOR_INPUT_PART.get()).build(null));

    public static final RegistryObject<BlockEntityType<ConveyorOutputPartBlockEntity>> CONVEYOR_OUTPUT_PART_ENTITY = BLOCK_ENTITIES.register("conveyor_output_part_entity",
            () -> BlockEntityType.Builder.of(ConveyorOutputPartBlockEntity::new,
                    BlockInit.CONVEYOR_OUTPUT_PART.get()).build(null));

    public static final TileEntityRegistryObject<TileEntityBoundingBlock> BOUNDING_BLOCK = TILE_ENTITY_TYPES.builder(BlockInit.BOUNDING_BLOCK, TileEntityBoundingBlock::new).build();

    public static final TileEntityRegistryObject<MinerMk1BlockEntity> MINER_MK1_BLOCK_ENTITY = TILE_ENTITY_TYPES.builder(BlockInit.MINER_MK1, MinerMk1BlockEntity::new).build();

//    public static final RegistryObject<BlockEntityType<MinerMk1BlockEntity>> MINER_MK1_BLOCK_ENTITY = BLOCK_ENTITIES.register("miner_mk1_block_entity",
//            () -> BlockEntityType.Builder.of(MinerMk1BlockEntity::new,
//                    BlockInit.MINER_MK1.get()).build(null));


}
