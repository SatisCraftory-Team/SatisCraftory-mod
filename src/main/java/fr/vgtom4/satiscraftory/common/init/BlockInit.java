package fr.vgtom4.satiscraftory.common.init;

import fr.vgtom4.satiscraftory.SatisCraftory;
import fr.vgtom4.satiscraftory.common.block.EliocubeBlock;
import fr.vgtom4.satiscraftory.common.block.MinerMK1Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockInit {
    private BlockInit(){
    }

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SatisCraftory.MODID);


    //Block with item
    public static final RegistryObject<Block> IRON_DEPOSIT = register("iron_deposit",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE)),
            new Item.Properties().tab(SatisCraftory.TAB));


    //Block without item
    public static final RegistryObject<Block> ELIOCUBE = registerBlockWithoutBlockItem("eliocube", () -> new EliocubeBlock(BlockBehaviour.Properties.of(Material.METAL).noOcclusion()));

    public static final RegistryObject<Block> MINER_MK1 = registerBlockWithoutBlockItem("miner_mk1", () -> new MinerMK1Block(BlockBehaviour.Properties.of(Material.METAL).noOcclusion()));

    public static final RegistryObject<Block> MINER_MK1_P2 = registerBlockWithoutBlockItem("miner_mk1_p2", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).noOcclusion()));
    public static final RegistryObject<Block> MINER_MK1_P3 = registerBlockWithoutBlockItem("miner_mk1_p3", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).noOcclusion()));




    //initialization with and without item
    private static <T extends Block> RegistryObject<T> registerBlockWithoutBlockItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> supplier, Item.Properties properties) {
        RegistryObject<T> block = BLOCKS.register(name, supplier);
        ItemInit.ITEMS.register(name, () -> new BlockItem(block.get(), properties));
        return block;
    }
}
