package fr.satiscraftoryteam.satiscraftory.common.block.resources;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class DepositBlock extends Block {
    private final Item residue;
    public DepositBlock(Item residue) {
        super(BlockBehaviour.Properties.of());
        this.residue = residue;
    }

    public Item getResidueExtracted() {
        return residue;
    }
}
