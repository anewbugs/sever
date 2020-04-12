package core.req;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Arrays;

public class Escrow {
    public String msgName;
    public byte[] msgByte;

    public Escrow(String msgName, byte[] msgByte) {
        this.msgName = msgName;
        this.msgByte = msgByte;
    }

    @Override
    public String toString() {
       return new ToStringBuilder( this )
               .append( new String(msgByte) )
               .toString();
    }
}
