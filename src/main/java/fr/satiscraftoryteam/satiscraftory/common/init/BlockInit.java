package fr.satiscraftoryteam.satiscraftory.common.init;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.block.*;
import fr.satiscraftoryteam.satiscraftory.common.block.base.BlockBounding;
import fr.satiscraftoryteam.satiscraftory.common.block.buildings.logistics.conveyors.ConveyorBlock;
import fr.satiscraftoryteam.satiscraftory.common.block.buildings.logistics.conveyors.ConveyorStreamPartBlock;
import fr.satiscraftoryteam.satiscraftory.common.block.buildings.production.miners.MinerMK1Block;
import fr.satiscraftoryteam.satiscraftory.common.block.buildings.production.smelters.SmelterBlock;
import fr.satiscraftoryteam.satiscraftory.common.block.examplesTest.EliocubeBlock;
import fr.satiscraftoryteam.satiscraftory.common.block.logistics.sorting.ConveyorMerger;
import fr.satiscraftoryteam.satiscraftory.common.builder.BlockBuilder;
import fr.satiscraftoryteam.satiscraftory.common.item.MinerMk1Item;
import fr.satiscraftoryteam.satiscraftory.common.registry.BlockRegistryObject;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.ConveyorInputPartBlockEntity;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.ConveyorOutputPartBlockEntity;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.ConveyorTileEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockInit {
    /**
    * NOTE: DON'T NEED TO ADD THE TAB PROPERTIES, IT'S AUTOMATICALLY ADDED IN {@link BlockBuilder#registerDefaultProperties)
    */
    public static final BlockBuilder BLOCKS = new BlockBuilder(SatisCraftory.MODID);

    public static final DeferredRegister<Block> SPETIAL_BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SatisCraftory.MODID);

    public static void register(IEventBus bus)  {
        SPETIAL_BLOCKS.register(bus);
        BLOCKS.register(bus);
    }

    // Default BlockItem
    public static final BlockRegistryObject<Block, BlockItem> BOUNDING_BLOCK =  BLOCKS.register("bounding_block", BlockBounding::new);
    public static final BlockRegistryObject<Block, BlockItem> CURVED_CONVEYOR = BLOCKS.register("curved_conveyor", () -> new Block(DefaultBlockMaterial.METAL));
    public static final BlockRegistryObject<Block, BlockItem> CONVEYOR_MERGER = BLOCKS.register("conveyor_merger", () -> new ConveyorMerger(DefaultBlockMaterial.METAL));
    public static final BlockRegistryObject<Block, BlockItem> MANU =            BLOCKS.register("manu", () -> new Block(DefaultBlockMaterial.METAL));
    public static final BlockRegistryObject<Block, BlockItem> IRON_DEPOSIT =    BLOCKS.register("iron_deposit", () -> new Block(DefaultBlockMaterial.STONE));
    public static final BlockRegistryObject<Block, BlockItem> COPPER_DEPOSIT =  BLOCKS.register("copper_deposit", () -> new Block(DefaultBlockMaterial.STONE));
    public static final BlockRegistryObject<Block, BlockItem> FOUNDATION_2M =   BLOCKS.register("foundation_2m", () -> new Foundations(DefaultBlockMaterial.METAL));

    public static final BlockRegistryObject<Block, BlockItem> PALE_BERRY_BUSH = BLOCKS.register("pale_berry_bush", () -> new PaleBerryBushBlock(BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH)));



    // no BlockItem
    //TODO: use BlockBuilder#register to register block with no Blockitem to get rid of SPETIAL_BLOCKS
    public static final RegistryObject<Block> ELIOCUBE = registerBlockWithoutBlockItem("eliocube", () -> new EliocubeBlock(BlockBehaviour.Properties.of(Material.METAL).noOcclusion()));
    public static final RegistryObject<Block> LOGO = registerBlockWithoutBlockItem("logo", () -> new LogoBlock(DefaultBlockMaterial.METAL));

    public static final BlockRegistryObject<ConveyorStreamPartBlock, BlockItem> CONVEYOR_INPUT_PART = BLOCKS.register("conveyor_input_part", () -> new ConveyorStreamPartBlock<>(BlockBehaviour.Properties.of(Material.METAL).noOcclusion(),ConveyorInputPartBlockEntity::new));
    public static final BlockRegistryObject<ConveyorStreamPartBlock, BlockItem> CONVEYOR_OUTPUT_PART = BLOCKS.register("conveyor_output_part", () -> new ConveyorStreamPartBlock<>(BlockBehaviour.Properties.of(Material.METAL).noOcclusion(), ConveyorOutputPartBlockEntity::new));

    public static final BlockRegistryObject<SmelterBlock, BlockItem> SMELTER =        BLOCKS.register("smelter", () -> new SmelterBlock(BlockBehaviour.Properties.of(Material.METAL).noOcclusion()));
    public static final BlockRegistryObject<MinerMK1Block, BlockItem> MINER_MK1 =     BLOCKS.register("miner_mk1", () -> new MinerMK1Block(BlockBehaviour.Properties.of(Material.METAL).noOcclusion()), (block) -> new MinerMk1Item(block, new Item.Properties()));
    public static final BlockRegistryObject<ConveyorBlock, BlockItem> CONVEYOR_FULL = BLOCKS.register("conveyor_full", () -> new ConveyorBlock(BlockBehaviour.Properties.of(Material.METAL).noOcclusion(), ((blockPos, blockState) -> new ConveyorTileEntity(blockPos, blockState, false))));
    public static final BlockRegistryObject<ConveyorBlock, BlockItem> CONVEYOR =      BLOCKS.register("conveyor", () -> new ConveyorBlock(BlockBehaviour.Properties.of(Material.METAL).noOcclusion(), ConveyorTileEntity::new));


    private static <T extends Block> RegistryObject<T> registerBlockWithoutBlockItem(String name, Supplier<T> block) {
        return SPETIAL_BLOCKS.register(name, block);
    }

    protected static class DefaultBlockMaterial {
        protected static BlockBehaviour.Properties STONE = BlockBehaviour.Properties.of(Material.STONE).noOcclusion();
        protected static BlockBehaviour.Properties METAL = BlockBehaviour.Properties.of(Material.METAL).noOcclusion();
    }
}
