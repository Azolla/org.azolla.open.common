package org.azolla.l.ling.cfg;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.azolla.l.ling.net.Url0;

import javax.annotation.Nonnull;
import java.net.URL;

/**
 * The coder is very lazy, nothing to write for this class
 *
 * @author sk@azolla.org
 * @since ADK1.0
 */
public interface LibCfg
{
    //String                    REGULAR_CFG_FILENAME = "^(.)*\\.Azolla.Cfg\\.(properties|xml)$";
    String                    REGULAR_CFG_FILENAME = "^(.)*\\.Azolla\\.(properties|xml)$";
    LoadingCache<String, URL> cfgFileCacheBuilder  = CacheBuilder.newBuilder().build(new CacheLoader<String, URL>()
    {
        @Override
        public URL load(@Nonnull String cfgFilename) throws Exception
        {

            if (cfgFilename.matches(REGULAR_CFG_FILENAME))
            {
                return Url0.getURL(cfgFilename);
            }
            else
            {
                throw new Exception("{" + cfgFilename + "} is not Azolla.org config.");
            }
        }
    });

    default void refresh(String cfgFilename)
    {
        cfgFileCacheBuilder.refresh(cfgFilename);
    }

    public void refresh();
}
