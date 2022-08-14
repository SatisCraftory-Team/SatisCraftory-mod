package fr.satiscraftoryteam.satiscraftory.common.builder;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.registry.BlockRegistryObject;
import fr.satiscraftoryteam.satiscraftory.common.registry.DoubleDeferredRegister;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockBuilder extends DoubleDeferredRegister<Block, Item> {


    public BlockBuilder(String modid) {
        super(modid, ForgeRegistries.BLOCKS, ForgeRegistries.ITEMS);
    }

    public BlockRegistryObject<Block, BlockItem> register(String name, BlockBehaviour.Properties properties) {
        return registerDefaultProperties(name, () -> new Block(properties), BlockItem::new);
    }

    public <BLOCK extends Block> BlockRegistryObject<BLOCK, BlockItem> register(String name, Supplier<? extends BLOCK> blockSupplier) {
        return registerDefaultProperties(name, blockSupplier, BlockItem::new);
    }

    public <BLOCK extends Block, ITEM extends BlockItem> BlockRegistryObject<BLOCK, ITEM> registerDefaultProperties(String name, Supplier<? extends BLOCK> blockSupplier,
                                                                                                                    BiFunction<BLOCK, Item.Properties, ITEM> itemCreator) {
        return register(name, blockSupplier, block -> itemCreator.apply(block, SatisCraftory.geBaseProperties()));
    }

    public <BLOCK extends Block, ITEM extends BlockItem> BlockRegistryObject<BLOCK, ITEM> register(String name, Supplier<? extends BLOCK> blockSupplier, Function<BLOCK, ITEM> itemCreator) {
        return register(name, blockSupplier, itemCreator, BlockRegistryObject::new);
    }
}
