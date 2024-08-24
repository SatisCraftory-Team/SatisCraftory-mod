package fr.satiscraftoryteam.satiscraftory.common.tileentity.capabilities;

import net.neoforged.neoforge.capabilities.BlockCapability;

import java.util.Optional;

public interface IBlockCapabilityProvider {
    <Q, C, T extends BlockCapability<Q, C>> Optional<Q> getCapability(T cap, C context);
}
