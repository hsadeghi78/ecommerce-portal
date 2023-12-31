package com.hs.ec.portal.domain.enumeration;

/**
 * application {
 * config {
 * baseName onlineStore,
 * applicationType monolith,
 * packageName com.hs.ecommerce,
 * authenticationType jwt,
 * prodDatabaseType mysql,
 * clientFramework angular
 * }
 * entities *
 * }
 */
public enum Performance {
    VERY_WEAK,
    WEAK,
    MIDDLE,
    WELL,
    PERFECT,
    EXCELLENT,
}
