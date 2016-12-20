package net;

import game.Move;

/**
 * Created by m.zilenas on 2016-12-16.
 */
public interface IMessageProcessor
{
    void process(String message);
    void process(String message, ServerConnection player);
    boolean isCaptureOnLastMove();
    Move getLastMove();
    void setLastMove(Move move);

}
