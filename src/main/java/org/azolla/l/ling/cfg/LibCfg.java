package org.azolla.l.ling.cfg;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.azolla.l.ling.io.File0;
import org.azolla.l.ling.net.Url0;

import javax.annotation.Nonnull;
import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * The coder is very lazy, nothing to write for this class
 *
 * @author sk@azolla.org
 * @since ADK1.0
 */
public interface LibCfg
{
    String                    REGULAR_CFG_FILENAME = "^(.)*\\.Azolla.Cfg\\.(properties|xml)$";
    LoadingCache<String, URL> cfgFileCacheBuilder  = CacheBuilder.newBuilder().build(new CacheLoader<String, URL>()
    {
        @Override
        public URL load(@Nonnull String pathStartWithSlash) throws Exception
        {

            if (pathStartWithSlash.matches(REGULAR_CFG_FILENAME))
            {
                return Url0.getURL(pathStartWithSlash);
            }
            else
            {
                throw new Exception("{" + pathStartWithSlash + "} is not Azolla.org config.");
            }
        }
    });

    default void refresh(String pathStartWithSlash)
    {
        cfgFileCacheBuilder.refresh(pathStartWithSlash);
    }

    public void refresh();
}
