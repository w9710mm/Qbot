package com.mm.qbot;

import com.mm.qbot.strategy.BilibiliParsingStrategy;
import org.junit.jupiter.api.Test;

public class ParsingStrategyTests {

    @Test
    void Bid() {
        try {
            BilibiliParsingStrategy.ParsingBID("BV1744y14794");
        } catch (BilibiliException e) {
            e.printStackTrace();
        }

    }
}
