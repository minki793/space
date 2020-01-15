package com.space.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

public enum ShipType {
    TRANSPORT,
    MILITARY,
    MERCHANT;

}