package com.penguin.aem.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(AemContextExtension.class)
class CarouselPenguinModelTest {

    private CarouselPenguinModel model;

    private Resource resource;

//    TODO: Simple test, of course can be expanded to cover all code.
    @BeforeEach
    public void setup(AemContext context) throws Exception {
        context.addModelsForClasses(CarouselPenguinModel.class);

        context.load().json("/models/carousel-penguin.json", "/content/penguin/en/test/jcr:content/root/responsivegrid/carouselpenguin");
        resource = context.resourceResolver().getResource("/content/penguin/en/test/jcr:content/root/responsivegrid/carouselpenguin");
        model = resource.adaptTo(CarouselPenguinModel.class);
    }

    @Test
    void testErrorPresent() throws Exception {
        assertEquals(1, model.getErrors().size());
    }

    @Test
    void testTilesPresent() throws Exception {
        assertEquals(5, model.getTiles().size());
    }

}