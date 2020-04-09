package core.until;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Log {
    public static final Logger core = LoggerFactory.getLogger("CORE");
    public static final Logger conn = LoggerFactory.getLogger( "CONN" );
    public static final Logger login = LoggerFactory.getLogger( "LOGIN" );
    public static final Logger game = LoggerFactory.getLogger( "GAME" );
    public static final Logger msg = LoggerFactory.getLogger( "MSG" );
}
