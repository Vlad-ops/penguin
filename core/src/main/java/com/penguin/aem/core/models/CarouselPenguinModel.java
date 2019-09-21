package com.penguin.aem.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Model(adaptables = Resource.class, resourceType = CarouselPenguinModel.RESOURCE_TYPE)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class CarouselPenguinModel implements ComponentExporter {

    static final String RESOURCE_TYPE = "penguin/components/content/carouselpenguin";

    @ChildResource
    @Optional
    private Resource carouselOptions;

    @Getter
    @Expose
    private List<Tile> tiles;

    @Getter
    private List<String> errors = new ArrayList<>();

    @PostConstruct
    public void postConstruct() {

        if (carouselOptions != null && carouselOptions.hasChildren()) {
            Iterator<Resource> childResources = carouselOptions.getChildren().iterator();
            tiles = new ArrayList<>();
            int index = 0;

            while (childResources.hasNext()) {
                index ++;
                Resource child = childResources.next();
                ValueMap valueMap = child.getValueMap();
                Tile tile = new Tile();
                tile.setAuthor(valueMap.get("author", String.class));
                tile.setImage(valueMap.get("image", String.class));
                tile.setTitle(valueMap.get("title", String.class));
                tile.setUrl(valueMap.get("url", String.class));

                if (valid(tile, index)) {
                    tiles.add(tile);
                }
            }
        } else {
            errors.add("This component has no content");
        }
    }

    //TODO validation can be moved to abstract class and reused across all models.
    private boolean valid(Tile tile, int index) {
        if (StringUtils.isBlank(tile.getAuthor())) {
            errors.add("The Carousel tile is missing a author at position " + index);
            return false;
        }
        if (StringUtils.isBlank(tile.getImage())) {
            //TODO dialog can be converted to file upload resourceType to allow full drag and drop support
            errors.add("The Carousel tile is missing a image at position " + index);
            return false;
        }
        if (StringUtils.isBlank(tile.getTitle())) {
            errors.add("The Carousel tile is missing a title at position " + index);
            return false;
        }
        if (StringUtils.isBlank(tile.getUrl())) {
            //TODO to make this more advanced, check if url is external and use external linkchecker service, if internal use resource resolver
            errors.add("The Carousel tile is missing a url at position " + index);
            return false;
        }

        return true;
    }

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }

    @Getter
    @Setter
    public class Tile {
        private String author;
        private String image;
        private String title;
        private String url;
    }
}
