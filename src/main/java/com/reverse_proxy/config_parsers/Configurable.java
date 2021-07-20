/*
 * Interface that every configurable object loaded from the YAML file must implement.
 */

package com.reverse_proxy.config_parsers;

/**
 * To easier identify and parse configurations loaded from the YAML file into a java object, every
 * object that holds YAML configurations must implement this interface.
 */
public interface Configurable {}
