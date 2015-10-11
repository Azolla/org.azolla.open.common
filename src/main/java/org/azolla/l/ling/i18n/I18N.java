/*
 * @(#)I18N.java		Created at 15/10/11
 * 
 * Copyright (c) azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.l.ling.i18n;

import org.azolla.l.ling.cfg.PropCfg;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The coder is very lazy, nothing to write for this class
 *
 * @author sk@azolla.org
 */
public class I18N
{
    PropCfg propCfg = null;

    public I18N(@Nullable PropCfg propCfg)
    {
        this.propCfg = propCfg;
    }

    public String get(@Nonnull String key)
    {
        return propCfg == null ? key : propCfg.get(key);
    }
}
