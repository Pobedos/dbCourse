package ru.pobedonostsev.framework.entity;

import java.io.Serializable;

public interface IEntity<PK> extends Serializable {
    PK getPK();
}
