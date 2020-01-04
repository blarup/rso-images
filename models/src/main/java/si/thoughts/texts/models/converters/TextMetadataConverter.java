package si.thoughts.texts.models.converters;

import si.thoughts.texts.lib.TextMetadata;
import si.thoughts.texts.models.entities.TextMetadataEntity;

public class TextMetadataConverter {

    public static TextMetadata toDto(TextMetadataEntity entity) {

        TextMetadata dto = new TextMetadata();
        dto.setTextId(entity.getId());
        dto.setCreated(entity.getCreated());
        dto.setDescription(entity.getDescription());
        dto.setTitle(entity.getTitle());
        return dto;
    }

    public static TextMetadataEntity toEntity(TextMetadata dto) {

        TextMetadataEntity entity = new TextMetadataEntity();
        entity.setCreated(dto.getCreated());
        entity.setDescription(dto.getDescription());
        entity.setTitle(dto.getTitle());
        return entity;

    }

}
