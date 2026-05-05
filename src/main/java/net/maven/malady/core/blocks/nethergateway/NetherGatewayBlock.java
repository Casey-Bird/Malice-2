package net.maven.malady.core.blocks.nethergateway;

import net.maven.malady.Malady;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class NetherGatewayBlock extends Block {

    public NetherGatewayBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            // Determine direction based on where player is looking
            Direction facing = getDirectionFromPlayer(player);

            // Create the portal - all at once
            createNetherPortal(level, pos, facing, player);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private Direction getDirectionFromPlayer(Player player) {
        // Get player's horizontal facing direction
        float yaw = player.getYRot();

        // Convert yaw to direction
        if (yaw < 0) {
            yaw += 360;
        }

        if (yaw >= 315 || yaw < 45) {
            return Direction.SOUTH;
        } else if (yaw >= 45 && yaw < 135) {
            return Direction.WEST;
        } else if (yaw >= 135 && yaw < 225) {
            return Direction.NORTH;
        } else {
            return Direction.EAST;
        }
    }

    private void createNetherPortal(Level level, BlockPos centerPos, Direction facing, Player player) {
        // Portal dimensions: 5x5
        int width = 5;
        int height = 5;

        // Determine portal orientation
        Direction.Axis axis = (facing == Direction.NORTH || facing == Direction.SOUTH) ?
                Direction.Axis.Z : Direction.Axis.X;

        // Position portal: move UP 1 more block from previous version
        // Bottom of portal should be at centerPos.getY() - 1 (was -2)
        int bottomY = centerPos.getY() - 1;

        // Calculate the bottom-left-front corner of the portal
        BlockPos bottomLeftFront = calculatePortalOrigin(centerPos, facing, bottomY);

        // Create everything at once - FORCE portal blocks to exist
        createForcedPortal(level, bottomLeftFront, axis, width, height);

        // Remove the gateway block
        level.setBlock(centerPos, Blocks.AIR.defaultBlockState(), 3);

        // Play effects
        playPortalEffects(level, centerPos, player);

        // Debug output
        debugPortalStructure(level, bottomLeftFront, axis, width, height);
    }

    private BlockPos calculatePortalOrigin(BlockPos centerPos, Direction facing, int bottomY) {
        // Simple positioning: portal is centered horizontally on gateway block
        // Bottom is at bottomY
        // For 5x5 portal, we need to offset 2 blocks left/back from center

        if (facing == Direction.NORTH) {
            // Portal faces north
            return new BlockPos(centerPos.getX() - 2, bottomY, centerPos.getZ() - 2);
        } else if (facing == Direction.SOUTH) {
            // Portal faces south
            return new BlockPos(centerPos.getX() - 2, bottomY, centerPos.getZ() + 2);
        } else if (facing == Direction.EAST) {
            // Portal faces east
            return new BlockPos(centerPos.getX() + 2, bottomY, centerPos.getZ() - 2);
        } else { // WEST
            // Portal faces west
            return new BlockPos(centerPos.getX() - 2, bottomY, centerPos.getZ() - 2);
        }
    }

    private void createForcedPortal(Level level, BlockPos origin, Direction.Axis axis, int width, int height) {
        // Method 1: Create frame first, then force portal blocks

        // Step 1: Create obsidian frame
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                BlockPos pos = getBlockPosition(origin, x, y, axis);

                // Clear block first
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);

                // Place obsidian on edges
                if (y == 0 || y == height - 1 || x == 0 || x == width - 1) {
                    level.setBlock(pos, Blocks.OBSIDIAN.defaultBlockState(), 3);
                }
            }
        }

        // Step 2: FORCE portal blocks into existence
        // Since you've disabled portal validation, we need to bypass it
        createPortalBlocksWithoutValidation(level, origin, axis, width, height);
    }

    private void createPortalBlocksWithoutValidation(Level level, BlockPos origin, Direction.Axis frameAxis, int width, int height) {
        // This method forces portal blocks to exist without validation
        // FIX: Portal blocks need the OPPOSITE axis of the frame for proper orientation

        // If frame extends along X axis (east-west), portal blocks should have Z axis (north-south strips)
        // If frame extends along Z axis (north-south), portal blocks should have X axis (east-west strips)
        Direction.Axis portalAxis = (frameAxis == Direction.Axis.X) ? Direction.Axis.Z : Direction.Axis.X;

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                BlockPos pos = getBlockPosition(origin, x, y, frameAxis);

                // Create portal block state with CORRECTED axis
                BlockState portalState = Blocks.NETHER_PORTAL.defaultBlockState()
                        .setValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_AXIS, portalAxis);

                // IMPORTANT: Use different flags to bypass validation
                // Flag 2 = BLOCK_UPDATE (no neighbor updates)
                // Flag 16 = UPDATE_NEIGHBORS
                // Combined: 18 = UPDATE_ALL

                // Try multiple approaches:

                // Approach 1: Direct placement with no validation
                level.setBlock(pos, portalState, 2); // Minimal updates

                // Check if it worked
                BlockState placed = level.getBlockState(pos);
                if (placed.getBlock() != Blocks.NETHER_PORTAL) {

                    // Approach 2: Use setBlockAndUpdate (more forceful)
                    level.setBlockAndUpdate(pos, portalState);

                    placed = level.getBlockState(pos);
                    if (placed.getBlock() != Blocks.NETHER_PORTAL) {

                        // Approach 3: Manually set block state without any checks
                        // This is the nuclear option
                        forceBlockState(level, pos, portalState);
                    }
                }
            }
        }
    }

    private void forceBlockState(Level level, BlockPos pos, BlockState state) {
        // Most forceful method - directly manipulate level
        try {
            // Get the chunk
            net.minecraft.world.level.chunk.LevelChunk chunk = level.getChunkAt(pos);

            // Set block state directly in chunk
            chunk.setBlockState(pos, state, false);

            // Mark chunk as dirty
            chunk.setUnsaved(true);

            // Update clients
            level.sendBlockUpdated(pos, level.getBlockState(pos), state, 3);

        } catch (Exception e) {
            Malady.LOGGER.error("Failed to force nether gateway block state at {}: {}", pos, e.getMessage());

            // Last resort: Use creative mode item placement logic
            level.setBlock(pos, state, 18); // UPDATE_ALL flag
        }
    }

    private BlockPos getBlockPosition(BlockPos origin, int x, int y, Direction.Axis axis) {
        if (axis == Direction.Axis.X) {
            // Portal extends along X axis (facing east/west)
            // x = north-south position, y = vertical
            return origin.offset(0, y, x);
        } else {
            // Portal extends along Z axis (facing north/south)
            // x = east-west position, y = vertical
            return origin.offset(x, y, 0);
        }
    }

    private void playPortalEffects(Level level, BlockPos pos, Player player) {
        // Play portal sound
        level.playSound(null, pos, SoundEvents.PORTAL_TRIGGER,
                SoundSource.BLOCKS, 1.0F, 1.0F);

        // Add portal particles
        if (level.isClientSide) {
            spawnPortalParticles(level, pos);
        }
    }

    private void spawnPortalParticles(Level level, BlockPos pos) {
        for (int i = 0; i < 100; i++) {
            double offsetX = (level.random.nextDouble() - 0.5) * 5.0;
            double offsetY = (level.random.nextDouble() - 0.5) * 5.0;
            double offsetZ = (level.random.nextDouble() - 0.5) * 5.0;

            level.addParticle(net.minecraft.core.particles.ParticleTypes.PORTAL,
                    pos.getX() + 0.5 + offsetX,
                    pos.getY() + 0.5 + offsetY,
                    pos.getZ() + 0.5 + offsetZ,
                    (level.random.nextDouble() - 0.5) * 0.5,
                    level.random.nextDouble() * 0.5,
                    (level.random.nextDouble() - 0.5) * 0.5);
        }
    }

    private void debugPortalStructure(Level level, BlockPos origin, Direction.Axis axis, int width, int height) {
        int portalCount = 0;
        int obsidianCount = 0;

        for (int y = height - 1; y >= 0; y--) {
            StringBuilder row = new StringBuilder();
            for (int x = 0; x < width; x++) {
                BlockPos pos = getBlockPosition(origin, x, y, axis);
                BlockState state = level.getBlockState(pos);
                char symbol;
                if (state.getBlock() == Blocks.OBSIDIAN) {
                    symbol = 'O';
                    obsidianCount++;
                } else if (state.getBlock() == Blocks.NETHER_PORTAL) {
                    symbol = 'P';
                    portalCount++;
                } else if (state.getBlock() == Blocks.AIR) {
                    symbol = '.';
                } else {
                    symbol = '?';
                }
                row.append(symbol).append(' ');
            }

        }
    }
}