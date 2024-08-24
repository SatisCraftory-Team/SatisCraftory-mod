package fr.satiscraftoryteam.satiscraftory.common.builder;

import com.google.common.base.Preconditions;
import fr.satiscraftoryteam.satiscraftory.common.registration.BlockRegistryObject;
import fr.satiscraftoryteam.satiscraftory.common.registration.DeferredBlockEntityCapabilityRegisterData;
import fr.satiscraftoryteam.satiscraftory.common.registration.TileEntityDeferredHolder;
import fr.satiscraftoryteam.satiscraftory.common.registration.WrappedDeferredRegister;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.TickableTileEntity;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.TileEntityUpdatable;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class TileEntityBuilder extends WrappedDeferredRegister<BlockEntityType<?>> {

    public TileEntityBuilder(String modid) {
        super(Registries.BLOCK_ENTITY_TYPE, modid, TileEntityDeferredHolder::new);
    }

    public <BE extends TileEntityUpdatable> BlockEntityTypeBuilder<BE> mekBuilder(BlockRegistryObject<?, ?> block, BlockEntityType.BlockEntitySupplier<? extends BE> factory) {
        BlockEntityTypeBuilder<BE> builder = this.<BE>builder(block, factory);
        return builder;
    }

    public <BE extends BlockEntity> BlockEntityTypeBuilder<BE> builder(BlockRegistryObject<?, ?> block, BlockEntityType.BlockEntitySupplier<? extends BE> factory) {
        return new BlockEntityTypeBuilder<>(block, factory);
    }

    public <BE extends TickableTileEntity> BlockEntityTypeBuilder<BE> builderAutoTick(BlockRegistryObject<?, ?> block, BlockEntityType.BlockEntitySupplier<? extends BE> factory) {
        BlockEntityTypeBuilder<BE> builder = new BlockEntityTypeBuilder<>(block, factory);
        return builder.clientTicker(TickableTileEntity::tickClient).serverTicker(TickableTileEntity::tickServer); //strange syntax but java don't like the simpler syntax
    }

    @SuppressWarnings("unchecked")
    private <BE extends BlockEntity> TileEntityDeferredHolder<BE> registerMek(String name, Supplier<? extends BlockEntityType<BE>> sup) {
        return (TileEntityDeferredHolder<BE>) super.register(name, sup);
    }

    @Override
    public void register(@NotNull IEventBus bus) {
        super.register(bus);
        bus.addListener(this::registerCapabilities);
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (DeferredHolder<BlockEntityType<?>, ? extends BlockEntityType<?>> entry : getEntries()) {
            //Note: All entries should be of this type
            if (entry instanceof TileEntityDeferredHolder<?> tileEntityDeferredHolder) {
                tileEntityDeferredHolder.registerCapabilityProviders(event);
            } else if (!FMLEnvironment.production) {
                throw new IllegalStateException("Expected entry to be a TileEntityTypeRegistryObject");
            }
        }
    }

    public class BlockEntityTypeBuilder<BE extends BlockEntity> {

        private final BlockRegistryObject<?, ?> block;
        private final BlockEntityType.BlockEntitySupplier<? extends BE> factory;
        //private final List<CapabilityData<BE, ?, ?>> capabilityProviders = new ArrayList<>();
        private final Set<DeferredBlockEntityCapabilityRegisterData<?, ?, BE>> deferredCapabilityRegisterData = new HashSet<>();
        @Nullable
        private BlockEntityTicker<BE> clientTicker;
        @Nullable
        private BlockEntityTicker<BE> serverTicker;

        BlockEntityTypeBuilder(BlockRegistryObject<?, ?> block, BlockEntityType.BlockEntitySupplier<? extends BE> factory) {
            this.block = block;
            this.factory = factory;
        }

//        public <CAP, CONTEXT> BlockEntityTypeBuilder<BE> withSimple(BlockCapability<CAP, CONTEXT> capability) {
//            return withSimple(capability, ConstantPredicates.ALWAYS_TRUE);
//        }
//
//        @SuppressWarnings("unchecked")
//        public <CAP, CONTEXT> BlockEntityTypeBuilder<BE> withSimple(BlockCapability<CAP, CONTEXT> capability, BooleanSupplier shouldApply) {
//            return with(capability, (ICapabilityProvider<? super BE, CONTEXT, CAP>) Capabilities.SIMPLE_PROVIDER, shouldApply);
//        }
//
//        public <CAP, CONTEXT> BlockEntityTypeBuilder<BE> with(BlockCapability<CAP, CONTEXT> capability,
//                                                              Function<BlockCapability<CAP, CONTEXT>, ICapabilityProvider<? super BE, CONTEXT, CAP>> provider) {
//            return with(capability, provider.apply(capability));
//        }
//
//        public <CAP, CONTEXT> BlockEntityTypeBuilder<BE> with(BlockCapability<CAP, CONTEXT> capability, ICapabilityProvider<? super BE, CONTEXT, CAP> provider) {
//            return with(capability, provider, ConstantPredicates.ALWAYS_TRUE);
//        }

//        /**
//         * @param shouldApply Determines whether the provider actually be attached to this block entity type. Useful for cases when we want to conditionally apply it
//         *                    based on loaded mods or a block's attributes.
//         */
//        public <CAP, CONTEXT> BlockEntityTypeBuilder<BE> with(BlockCapability<CAP, CONTEXT> capability, ICapabilityProvider<? super BE, CONTEXT, CAP> provider,
//                                                              BooleanSupplier shouldApply) {
//            capabilityProviders.add(new CapabilityData<>(capability, provider, shouldApply));
//            return this;
//        }
//
//        public BlockEntityTypeBuilder<BE> without(BlockCapability<?, ?>... capabilities) {
//            for (BlockCapability<?, ?> capability : capabilities) {
//                //noinspection Java8CollectionRemoveIf - We can't replace it with removeIf as it has a capturing lambda
//                for (Iterator<CapabilityData<BE, ?, ?>> iterator = capabilityProviders.iterator(); iterator.hasNext(); ) {
//                    if (iterator.next().capability() == capability) {
//                        iterator.remove();
//                    }
//                }
//            }
//            return this;
//        }
//
//        public BlockEntityTypeBuilder<BE> without(Collection<? extends BlockCapability<?, ?>> capabilities) {
//            //noinspection Java8CollectionRemoveIf - We can't replace it with removeIf as it has a capturing lambda
//            for (Iterator<CapabilityData<BE, ?, ?>> iterator = capabilityProviders.iterator(); iterator.hasNext(); ) {
//                if (capabilities.contains(iterator.next().capability())) {
//                    iterator.remove();
//                }
//            }
//            return this;
//        }

        public BlockEntityTypeBuilder<BE> clientTicker(BlockEntityTicker<BE> ticker) {
            Preconditions.checkState(clientTicker == null, "Client ticker may only be set once.");
            clientTicker = ticker;
            return this;
        }

        public BlockEntityTypeBuilder<BE> serverTicker(BlockEntityTicker<BE> ticker) {
            Preconditions.checkState(serverTicker == null, "Server ticker may only be set once.");
            serverTicker = ticker;
            return this;
        }

        public BlockEntityTypeBuilder<BE> commonTicker(BlockEntityTicker<BE> ticker) {
            return clientTicker(ticker)
                    .serverTicker(ticker);
        }

        public <T, C, BC extends BlockCapability<T, C>> BlockEntityTypeBuilder<BE> withCapability(BC capability, ICapabilityProvider<BE, C, T> capabilityProvider){
            deferredCapabilityRegisterData.add(new DeferredBlockEntityCapabilityRegisterData<>(capability, capabilityProvider));
            return this;
        }

        @SuppressWarnings("ConstantConditions")
        public TileEntityDeferredHolder<BE> build() {
            //Note: There is no data fixer type as forge does not currently have a way exposing data fixers to mods yet
            TileEntityDeferredHolder<BE> holder = registerMek(block.getName(), () -> BlockEntityType.Builder.<BE>of(factory, block.getBlock()).build(null));
            holder.tickers(clientTicker, serverTicker);
            holder.capabilityProviders(deferredCapabilityRegisterData);
            return holder;
        }

    }
}