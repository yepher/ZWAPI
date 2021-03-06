
package zwapi.nodes;

import javax.swing.event.EventListenerList;

import zwapi.NoAssociatedNetworkException;
import zwapi.ZWFrame;
import zwapi.ZWNetwork;
import zwapi.events.ZWBasicListener;
import zwapi.events.ZWEvent;
import zwapi.events.ZWEventListener;
import zwapi.events.ZWNodeInfoListener;
import zwapi.events.ZWVersionListener;

/**
 * The basic node class used to model a node in the z-wave network
 * 
 * @author Andreas Møller
 * @author David Emil Lemvigh
 */
public class ZWNode {

    /** reference to the node's associated network */
    protected ZWNetwork network;

    /** ID of the node */
    protected byte id;

    protected EventListenerList listeners;

    /**
     * The constructor
     * 
     * @param id
     */
    public ZWNode(int id) {
        this.id = (byte) id;
        this.listeners = new EventListenerList();
    }

    /**
     * @return z-wave node id
     */
    public int getID() {
        return this.id;
    }

    /**
     * Sets the network the the node is associated with... this is automatically done when adding the node to a network
     * 
     * @param network
     */
    /** associates a node with its network */
    public void setNetwork(ZWNetwork network) {
        this.network = network;
    }

    /**
     * Sends a basic set command to the node
     * 
     * @param b
     *            on/off value (on=255, off=0)
     * @throws NoAffiliatedNetworkException
     */
    public void basicSet(boolean b) throws NoAssociatedNetworkException {
        byte value = (byte) (b ? 0xFF : 0x00);
        basicSet(value);
    }

    /**
     * Sends a basic set command to the node
     * 
     * @param value
     *            specific value from 0-255
     * @throws NoAssociatedNetworkException
     */
    public void basicSet(byte value) throws NoAssociatedNetworkException {
        if (this.network == null) {
            throw new NoAssociatedNetworkException();
        }

        this.network.sendData(this.id, new byte[] {ZWFrame.COMMAND_CLASS_BASIC, ZWFrame.BASIC_SET, value});
    }

    /**
     * @param listener
     */
    public void addZWEventListener(ZWEventListener listener) {
        this.listeners.add(ZWEventListener.class, listener);
    }

    /**
     * @param listener
     */
    public void removeZWEventListener(ZWEventListener listener) {
        this.listeners.remove(ZWEventListener.class, listener);
    }

    /**
     * @param listener
     */
    public void addZWBasicListener(ZWBasicListener listener) {
        this.listeners.add(ZWBasicListener.class, listener);
    }

    /**
     * @param listener
     */
    public void removeZWBasicListener(ZWBasicListener listener) {
        this.listeners.remove(ZWBasicListener.class, listener);
    }

    /**
     * Fires a ZWEvent
     * 
     * @param event
     */
    public void fireZWEvent(ZWEvent event) {
        Object[] l = this.listeners.getListenerList();
        // loop through each listener and pass on the event if needed
        int numListeners = l.length;
        for (int i = 0; i < numListeners; i += 2) {
            if (l[i] == ZWEventListener.class) {
                // pass the event to the listeners event dispatch method
                ((ZWEventListener) l[i + 1]).ZWEventOccurred(event);
            }
        }
    }

    /**
     * fires a ZWBasicEvent
     * 
     * @param event
     */
    public void fireZWBasicEvent(ZWEvent event) {
        Object[] l = this.listeners.getListenerList();
        // loop through each listener and pass on the event if needed
        int numListeners = l.length;
        for (int i = 0; i < numListeners; i += 2) {
            if (l[i] == ZWBasicListener.class) {
                // pass the event to the listeners event dispatch method
                ((ZWBasicListener) l[i + 1]).ZWBasicEventOccurred(event);
            }
        }
        fireZWEvent(event);
    }

    public void addZWNodeInfoListener(ZWNodeInfoListener listener) {
        this.listeners.add(ZWNodeInfoListener.class, listener);
    }

    public void removeZWNodeInfoListener(ZWNodeInfoListener listener) {
        this.listeners.remove(ZWNodeInfoListener.class, listener);
    }

    /**
     * fires a ZWBasicEvent
     * 
     * @param event
     */
    public void fireZWNodeInfoEvent(ZWEvent event) {
        Object[] l = this.listeners.getListenerList();
        // loop through each listener and pass on the event if needed
        int numListeners = l.length;
        for (int i = 0; i < numListeners; i += 2) {
            if (l[i] == ZWBasicListener.class) {
                // pass the event to the listeners event dispatch method
                ((ZWNodeInfoListener) l[i + 1]).ZWNodeInfoEventOccurred(event);
            }
        }
        fireZWEvent(event);
    }

    /**
     * @param listener
     */
    public void addZWVersionListener(ZWVersionListener listener) {
        this.listeners.add(ZWVersionListener.class, listener);
    }

    /**
     * @param listener
     */
    public void removeZWVersionListener(ZWVersionListener listener) {
        this.listeners.remove(ZWVersionListener.class, listener);
    }

    /**
     * fire a ZWVersionEvent
     * 
     * @param event
     */
    public void fireZWVersionEvent(ZWEvent event) {
        Object[] l = this.listeners.getListenerList();
        // loop through each listener and pass on the event if needed
        int numListeners = l.length;
        for (int i = 0; i < numListeners; i += 2) {
            if (l[i] == ZWVersionListener.class) {
                // pass the event to the listeners event dispatch method
                ((ZWVersionListener) l[i + 1]).ZWVersionEventOccurred(event);
            }
        }
        fireZWEvent(event);
    }

}
