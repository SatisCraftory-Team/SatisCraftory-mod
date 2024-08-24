package fr.satiscraftoryteam.satiscraftory.common.init;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.block.base.BlockBounding;
import fr.satiscraftoryteam.satiscraftory.common.block.buildings.Foundations;
import fr.satiscraftoryteam.satiscraftory.common.block.buildings.logistics.conveyors.ConveyorBlock;
import fr.satiscraftoryteam.satiscraftory.common.block.buildings.logistics.conveyors.ConveyorStreamPartBlock;
import fr.satiscraftoryteam.satiscraftory.common.block.buildings.logistics.sorting.ConveyorMerger;
import fr.satiscraftoryteam.satiscraftory.common.block.buildings.production.miners.NewMinerMk1Block;
import fr.satiscraftoryteam.satiscraftory.common.block.buildings.production.smelters.SmelterBlock;
import fr.satiscraftoryteam.satiscraftory.common.block.buildings.specials.LogoBlock;
import fr.satiscraftoryteam.satiscraftory.common.block.examplesTest.EliocubeBlock;
import fr.satiscraftoryteam.satiscraftory.common.block.flora.PaleBerryBushBlock;
import fr.satiscraftoryteam.satiscraftory.common.builder.BlockBuilder;
import fr.satiscraftoryteam.satiscraftory.common.item.EliocubeItem;
import fr.satiscraftoryteam.satiscraftory.common.item.LogoItem;
import fr.satiscraftoryteam.satiscraftory.common.item.MinerMk1Item;
import fr.satiscraftoryteam.satiscraftory.common.item.SmelterItem;
import fr.satiscraftoryteam.satiscraftory.common.registration.BlockRegistryObject;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.conveyor.ConveyorInputPartBlockEntity;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.conveyor.ConveyorOutputPartBlockEntity;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.conveyor.ConveyorTileEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockInit {
    /**
    * NOTE: DON'T NEED TO ADD THE TAB PROPERTIES, IT'S AUTOMATICALLY ADDED IN {@link BlockBuilder#registerDefaultProperties)
    */
    public static final BlockBuilder BLOCKS = new BlockBuilder(SatisCraftory.MODID);

    public static void register(IEventBus bus)  {
        BLOCKS.register(bus);
    }

    public static final DeferredRegister.Blocks BLOCKSs = DeferredRegister.createBlocks(SatisCraftory.MODID);

    // Default BlockItem
    public static final BlockRegistryObject<Block, BlockItem> BOUNDING_BLOCK =  BLOCKS.register("bounding_block", BlockBounding::new);
    public static final BlockRegistryObject<Block, BlockItem> CURVED_CONVEYOR = BLOCKS.register("curved_conveyor", () -> new Block(BlockBehaviour.Properties.of()));
    public static final BlockRegistryObject<Block, BlockItem> CONVEYOR_MERGER = BLOCKS.register("conveyor_merger", () -> new ConveyorMerger(BlockBehaviour.Properties.of()));
    public static final BlockRegistryObject<Block, BlockItem> MANU =            BLOCKS.register("manu", () -> new Block(BlockBehaviour.Properties.of().noOcclusion()));
    public static final BlockRegistryObject<Block, BlockItem> IRON_DEPOSIT =    BLOCKS.register("iron_deposit", () -> new Block(BlockBehaviour.Properties.of()));
    public static final BlockRegistryObject<Block, BlockItem> COPPER_DEPOSIT =  BLOCKS.register("copper_deposit", () -> new Block(BlockBehaviour.Properties.of()));
    public static final BlockRegistryObject<Block, BlockItem> FOUNDATION_2M =   BLOCKS.register("foundation_2m", () -> new Foundations(BlockBehaviour.Properties.of()));

    public static final BlockRegistryObject<Block, BlockItem> PALE_BERRY_BUSH = BLOCKS.register("pale_berry_bush", () -> new PaleBerryBushBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH)));
    public static final BlockRegistryObject<Block, BlockItem> ELIOCUBE =        BLOCKS.register("eliocube", () -> new EliocubeBlock(BlockBehaviour.Properties.of().noOcclusion()), (block) -> new EliocubeItem(block, new Item.Properties()));
    public static final BlockRegistryObject<Block, BlockItem> LOGO =            BLOCKS.register("logo", () -> new LogoBlock(BlockBehaviour.Properties.of().noOcclusion()), (block) -> new LogoItem(block, new Item.Properties()));

    public static final BlockRegistryObject<ConveyorStreamPartBlock, BlockItem> CONVEYOR_INPUT_PART = BLOCKS.register("conveyor_input_part", () -> new ConveyorStreamPartBlock<>(BlockBehaviour.Properties.of().noOcclusion(), ConveyorInputPartBlockEntity::new));
    public static final BlockRegistryObject<ConveyorStreamPartBlock, BlockItem> CONVEYOR_OUTPUT_PART = BLOCKS.register("conveyor_output_part", () -> new ConveyorStreamPartBlock<>(BlockBehaviour.Properties.of().noOcclusion(), ConveyorOutputPartBlockEntity::new));
    public static final BlockRegistryObject<NewMinerMk1Block, BlockItem> MINER_MK1 =     BLOCKS.register("miner_mk1", NewMinerMk1Block::new, (block) -> new MinerMk1Item(block, new Item.Properties()));

    public static final BlockRegistryObject<SmelterBlock, BlockItem> SMELTER =        BLOCKS.register("smelter", () -> new SmelterBlock(BlockBehaviour.Properties.of().noOcclusion()), (block) -> new SmelterItem(block, new Item.Properties()));
    public static final BlockRegistryObject<ConveyorBlock, BlockItem> CONVEYOR_FULL = BLOCKS.register("conveyor_full", () -> new ConveyorBlock(BlockBehaviour.Properties.of().noOcclusion(), ((blockPos, blockState) -> new ConveyorTileEntity(blockPos, blockState, false))));
    public static final BlockRegistryObject<ConveyorBlock, BlockItem> CONVEYOR =      BLOCKS.register("conveyor", () -> new ConveyorBlock(BlockBehaviour.Properties.of().noOcclusion(), ConveyorTileEntity::new));
}
