package study.datajpa.entity;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;

public class BaseEntityUtil {
    protected static boolean isEntityAttribute(EntityManager em,
                                               Class clazz,
                                               String attributeName) {
        return em.getEntityManagerFactory()
                .getMetamodel()
                .getEntities()
                .stream()
                .filter(entityType -> {
                    return entityType.getJavaType() == clazz &&
                            isPresentAttribute(attributeName, entityType);
                })
                .findAny()
                .isPresent();
    }

    private static boolean isPresentAttribute(String attributeName,
                                              EntityType<?> entityType) {
        return entityType.getAttributes()
                .stream()
                .filter(attribute -> attributeName.equals(attribute.getName()))
                .findAny()
                .isPresent();
    }
}
