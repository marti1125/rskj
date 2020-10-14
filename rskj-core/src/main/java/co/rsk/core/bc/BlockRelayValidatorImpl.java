/*
 * This file is part of RskJ
 * Copyright (C) 2017 RSK Labs Ltd.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package co.rsk.core.bc;

import co.rsk.validators.*;
import org.ethereum.core.Block;
import org.ethereum.core.BlockHeader;
import org.ethereum.db.BlockStore;

import javax.annotation.Nonnull;

/**
 * Validates a block if it is good enough to be propagated.
 */
public class BlockRelayValidatorImpl implements BlockValidator {

    private final BlockStore blockStore;

    private final BlockHeaderValidationRule blockHeaderValidator;

    private final BlockHeaderParentDependantValidationRule blockParentValidator;

    private final BlockValidationRule blockValidator;

    public BlockRelayValidatorImpl(@Nonnull BlockStore blockStore,
                                   @Nonnull BlockHeaderValidationRule blockHeaderValidator,
                                   @Nonnull BlockHeaderParentDependantValidationRule blockParentValidator,
                                   @Nonnull BlockValidationRule blockValidator) {
        this.blockStore = blockStore;
        this.blockHeaderValidator = blockHeaderValidator;
        this.blockParentValidator = blockParentValidator;
        this.blockValidator = blockValidator;
    }

    /**
     * Validate a block.
     * The validation includes:
     * - validation of the header data of the block
     * - validation of the block
     * - validation of the header data of the parent block
     *
     * @param block Block to validate
     * @return true if the block is valid, otherwise - false.
     */
    @Override
    public boolean isValid(@Nonnull Block block) {
        if (block.isGenesis()) {
            return true;
        }

        BlockHeader header = block.getHeader();
        if (!blockHeaderValidator.isValid(header)) {
            return false;
        }

        if (!blockValidator.isValid(block)) {
            return false;
        }

        Block parent = getParent(block);
        return parent != null && blockParentValidator.isValid(header, parent);
    }

    private Block getParent(Block block) {
        return blockStore.getBlockByHash(block.getParentHash().getBytes());
    }
}