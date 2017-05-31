package com.github.rcf.registry;

import java.util.List;

/**
 * @author winstone
 *
 */
public interface ChildListener {

    void childChanged(String path, List<String> children);

}