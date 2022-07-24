package fr.vgtom4.satiscraftory.init;

import fr.vgtom4.satiscraftory.SatisCraftory;
import fr.vgtom4.satiscraftory.block.entity.custom.EliocubeEntity;
import fr.vgtom4.satiscraftory.block.entity.custom.MinerMk1BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SatisCraftory.MODID);

    public static final RegistryObject<BlockEntityType<MinerMk1BlockEntity>> MINER_MK1_BLOCK_ENTITY = BLOCK_ENTITIES.register("miner_mk1_block_entity",
            () -> BlockEntityType.Builder.of(MinerMk1BlockEntity::new,
                    BlockInit.MINER_MK1.get()).build(null));

    public static final RegistryObject<BlockEntityType<EliocubeEntity>> ELIOCUBE_ENTITY = BLOCK_ENTITIES.register("eliocube_entity",
            () -> BlockEntityType.Builder.of(EliocubeEntity::new,
                    BlockInit.ELIOCUBE.get()).build(null));

}