package fr.vgtom4.satiscraftory.common.init;

import fr.vgtom4.satiscraftory.SatisCraftory;
import fr.vgtom4.satiscraftory.common.block.*;
import fr.vgtom4.satiscraftory.common.block.base.BlockBounding;
import fr.vgtom4.satiscraftory.common.builder.BlockBuilder;
import fr.vgtom4.satiscraftory.common.registry.BlockRegistryObject;
import fr.vgtom4.satiscraftory.common.tileentity.ConveyorInputPartBlockEntity;
import fr.vgtom4.satiscraftory.common.tileentity.ConveyorOutputPartBlockEntity;
import fr.vgtom4.satiscraftory.common.tileentity.ConveyorTileEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockInit {

    public static final BlockBuilder BLOCKS_TEST = new BlockBuilder(SatisCraftory.MODID);

    private BlockInit(){
    }

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SatisCraftory.MODID);

    public static final BlockRegistryObject<BlockBounding, BlockItem> BOUNDING_BLOCK = registerBoundingBlock("bounding_block", BlockBounding::new);


    // ------------ Block with item ------------

    public static final RegistryObject<Block> CURVED_CONVEYOR = register("curved_conveyor", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).noOcclusion()), new Item.Properties().tab(SatisCraftory.TAB));
    public static final RegistryObject<Block> CONVEYOR_MERGER = register("conveyor_merger", () -> new ConveyorMerger(BlockBehaviour.Properties.of(Material.METAL).noOcclusion()));
    public static final RegistryObject<Block> MANU = register("manu", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).noOcclusion()), new Item.Properties().tab(SatisCraftory.TAB));



    public static final RegistryObject<Block> IRON_DEPOSIT = register("iron_deposit", () -> new Block(BlockBehaviour.Properties.of(Material.STONE)));
    public static final RegistryObject<Block> COPPER_DEPOSIT = register("copper_deposit", () -> new Block(BlockBehaviour.Properties.of(Material.STONE)));

    // Building
    public static final RegistryObject<Block> FOUNDATION_2M = register("foundation_2m", () -> new Foundations(BlockBehaviour.Properties.of(Material.METAL)));


    // ------------ Block without item ------------
    public static final RegistryObject<Block> ELIOCUBE = registerBlockWithoutBlockItem("eliocube", () -> new EliocubeBlock(BlockBehaviour.Properties.of(Material.METAL).noOcclusion()));
    public static final RegistryObject<Block> LOGO = registerBlockWithoutBlockItem("logo", () -> new LogoBlock(BlockBehaviour.Properties.of(Material.METAL).noOcclusion()));

    // something very stupid happened here
    public static final RegistryObject<Block> PALE_BERRY_BUSH = register("pale_berry_bush", () -> new PaleBerryBushBlock(BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH)));



    //public static final RegistryObject<Block> MINER_MK1 = registerBlockWithoutBlockItem("miner_mk1", () -> new MinerMK1Block(BlockBehaviour.Properties.of(Material.METAL).noOcclusion()));
    public static final BlockRegistryObject<ConveyorStreamPartBlock, BlockItem> CONVEYOR_INPUT_PART = BLOCKS_TEST.register("conveyor_input_part", () -> new ConveyorStreamPartBlock<>(BlockBehaviour.Properties.of(Material.METAL).noOcclusion(),ConveyorInputPartBlockEntity::new));
    public static final BlockRegistryObject<ConveyorStreamPartBlock, BlockItem> CONVEYOR_OUTPUT_PART = BLOCKS_TEST.register("conveyor_output_part", () -> new ConveyorStreamPartBlock<>(BlockBehaviour.Properties.of(Material.METAL).noOcclusion(), ConveyorOutputPartBlockEntity::new));

    public static final BlockRegistryObject<MinerMK1Block, BlockItem> MINER_MK1 = BLOCKS_TEST.register("miner_mk1", () -> new MinerMK1Block(BlockBehaviour.Properties.of(Material.METAL).noOcclusion()));
    public static final BlockRegistryObject<ConveyorBlock, BlockItem> CONVEYOR_FULL = BLOCKS_TEST.register("conveyor_full", () -> new ConveyorBlock(BlockBehaviour.Properties.of(Material.METAL).noOcclusion(), ((blockPos, blockState) -> new ConveyorTileEntity(blockPos, blockState, false))));
    public static final BlockRegistryObject<ConveyorBlock, BlockItem> CONVEYOR = BLOCKS_TEST.register("conveyor", () -> new ConveyorBlock(BlockBehaviour.Properties.of(Material.METAL).noOcclusion(), ConveyorTileEntity::new));


    private static BlockRegistryObject<BlockBounding, BlockItem> registerBoundingBlock(String name, Supplier<BlockBounding> blockSupplier) {
        return BLOCKS_TEST.register(name, blockSupplier, block -> new BlockItem(block, new Item.Properties()));
    }
    //initialization with and without item
    private static <T extends Block> RegistryObject<T> registerBlockWithoutBlockItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> supplier, Item.Properties properties) {
        RegistryObject<T> block = BLOCKS.register(name, supplier);
        ItemInit.ITEMS.register(name, () -> new BlockItem(block.get(), properties));
        return block;
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> supplier) {
        RegistryObject<T> block = BLOCKS.register(name, supplier);
        ItemInit.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(SatisCraftory.TAB)));
        return block;
    }
}
