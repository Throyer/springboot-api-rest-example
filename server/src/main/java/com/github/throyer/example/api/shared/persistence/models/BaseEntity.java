package com.github.throyer.example.api.shared.persistence.models;

import jakarta.persistence.MappedSuperclass;

import static com.github.throyer.example.api.utils.ID.decode;
import static com.github.throyer.example.api.utils.ID.encode;

@MappedSuperclass
public abstract class BaseEntity {
  public abstract Long getId();
  public abstract void setId(Long id);

  public void setId(String id) {
    this.setId(decode(id));
  }

  public String getStringId() {
    return encode(this.getId());
  }
}
