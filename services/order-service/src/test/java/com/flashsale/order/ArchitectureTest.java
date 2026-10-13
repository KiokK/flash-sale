package com.flashsale.order;

import com.flashsale.testkit.HexagonalArchitecture;
import org.junit.jupiter.api.Test;

class ArchitectureTest {

    @Test
    void layersRespectHexagonalBoundaries() {
        HexagonalArchitecture.assertLayersRespected("com.flashsale.order");
    }
}
