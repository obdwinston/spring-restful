package com.example.database.mappers;

// This mapper interface is useful if we want to change the mapping library.
public interface Mapper<Entity, Dto> {

    Dto mapTo(Entity entity);

    Entity mapFrom(Dto dto);
}
