package com.chrisrm.ideaddlc.actions.positions

import com.chrisrm.ideaddlc.config.enums.TabHighlightPositions

class MTToplessPosition : MTAbstractPositionsAction() {
  override fun getPosition(): TabHighlightPositions =
      TabHighlightPositions.TOPLESS // Heh, topless

}